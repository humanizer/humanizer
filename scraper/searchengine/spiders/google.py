#!/usr/bin/env python
# encoding: utf-8

from bs4 import BeautifulSoup
import codecs
import couchdb
from scrapy.selector import HtmlXPathSelector
from scrapy.spider import BaseSpider
import urllib
import urlparse

from searchengine.items import SearchengineItem

LANGUAGE = "vi"
LOCALE = "vn"

def ConvertQueryToUrl(query):
    if isinstance(query, str):
        query = unicode(query) 
    parameters = { 'q' : query.encode('utf-8'),
                   'hl' : LANGUAGE,
                   'gl' : LOCALE }
    return 'http://www.google.com/search?' + urllib.urlencode(parameters)

def ConvertUrlToQuery(url):
    urlQuery = urlparse.urlparse(url).query
    parameters = dict(urlparse.parse_qsl(urlQuery))
    query = parameters["q"]
    return urllib.unquote_plus(query)

def cleanText(data):
    if type(data) == list:
        return map(cleanText, data)
    elif type(data) in [str, unicode] :
        # return lxml.html.fromstring(data).text_content()
        return ' '.join(BeautifulSoup(data).findAll(text=True))
    else:
        return data

def loadQueryFromFile(queryFile):
    f = codecs.open(queryFile, encoding='utf-8')
    return [line.strip() for line in f]
        
class GoogleSpider(BaseSpider):
    name = 'google'
    allowed_domains = ['google.com']
    start_urls = []
    couch = None
    
    def __init__(self, query_file = None, project_id = None, **kwargs):
        assert query_file is not None
        assert project_id is not None
        self.project_id = project_id
        self.queries = loadQueryFromFile(query_file)
        print "Loaded %d queries." % len(self.queries)
        GoogleSpider.start_urls = map(ConvertQueryToUrl, self.queries)
        GoogleSpider.couch = couchdb.Server("http://humanizer:123456@humanizer.iriscouch.com")
        assert self.isValidProjectId(GoogleSpider.couch["projects"], 
                                     self.project_id)
        self.tasksDb = GoogleSpider.couch["tasks"]
        self.itemsDb = GoogleSpider.couch["items"]

    def isValidProjectId(self, projectDb, project_id):
        try:
            doc = projectDb[project_id]
            print "Scraping for project ", doc
            return True
        except:
            return False
        
        
    def parse(self, response):
        hxs = HtmlXPathSelector(response)
        
        center_column = hxs.select("//div[@id='ires']") 
        if not center_column:
            return
        
        query = ConvertUrlToQuery(response.url)
        task = {"params" : { "type": "search",
                             "query" : query,
                             "engine" : "google",
                             "language": LANGUAGE,
                             "locale": LOCALE },
                "project_id" : self.project_id,
                "raters" : [],
                "status" : {},
                "type" : "search",
                "title" : "Google: " + query}        
        task_id, _ = self.tasksDb.save(task)
        print "Saved task ", task_id

        position = 0
        for result in center_column.select("//li[@class='g']"):
            i = SearchengineItem()
            title = result.select(".//h3[@class='r']/a")
            if title:
                i['title']= cleanText(title.extract()[0])
                i['url'] = title.select("@href").extract()[0]
            snippet = result.select(".//span[@class='st']")
            if snippet:
                i['snippet'] = snippet.extract()[0]
            if i.is_valid():
                position += 1
                i['position'] = position
                docid, _ = self.itemsDb.save({"name" : i['url'], "value" : dict(i), "task_id" : task_id})
                print "Extracted ", i, "saved to", docid
        
        # Local result
        localBlock = hxs.select("//div[contains(@class,'kno-fb-ctx')]")
        if localBlock:
            print "Got local result"
            divHtml = localBlock[0].extract()
            docid, _ = self.itemsDb.save({"name" : "local_result", "value" : divHtml, "task_id" : task_id})
            print "Extracted local result saved to", docid
            
