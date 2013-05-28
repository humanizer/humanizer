#!/usr/bin/env python
# encoding: utf-8

from bs4 import BeautifulSoup
import codecs
import couchdb
from scrapy.selector import HtmlXPathSelector
from scrapy.spider import BaseSpider
import urllib
import urlparse
import simplejson

from searchengine.items import SearchengineItem

LANGUAGE = "vi"
LOCALE = "vn"

def ConvertQueryToUrl(query):
    if isinstance(query, str):
        query = unicode(query) 
    parameters = { 'query' : query.encode('utf-8') }
    return 'http://coccoc.com/instant/search.json?' + urllib.urlencode(parameters)

def ConvertUrlToQuery(url):
    urlQuery = urlparse.urlparse(url).query
    parameters = dict(urlparse.parse_qsl(urlQuery))
    query = parameters["query"]
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
        
class CoccocSpider(BaseSpider):
    name = 'coccoc'
    allowed_domains = ['coccoc.com']
    start_urls = []
    couch = None
    
    def __init__(self, query_file = None, project_id = None, **kwargs):
        assert query_file is not None
        assert project_id is not None
        self.project_id = project_id
        self.queries = loadQueryFromFile(query_file)
        print "Loaded %d queries." % len(self.queries)
        CoccocSpider.start_urls = map(ConvertQueryToUrl, self.queries)
        CoccocSpider.couch = couchdb.Server("http://humanizer:123456@humanizer.iriscouch.com")
        assert self.isValidProjectId(CoccocSpider.couch["projects"], 
                                     self.project_id)
        self.tasksDb = CoccocSpider.couch["tasks"]
        self.itemsDb = CoccocSpider.couch["items"]

    def isValidProjectId(self, projectDb, project_id):
        try:
            doc = projectDb[project_id]
            print "Scraping for project ", doc
            return True
        except:
            return False
        
        
    def parse(self, response):
#         import pdb;pdb.set_trace()
        json = simplejson.loads(response.body)
        if not json.get('result'):
            print "Empty result"
            return
        results = json.get('result')
        query = ConvertUrlToQuery(response.url)
        task = {"params" : { "type": "search",
                             "query" : query,
                             "engine" : "coccoc",
                             "language": LANGUAGE,
                             "locale": LOCALE },
                "project_id" : self.project_id,
                "raters" : [],
                "status" : {},
                "type" : "search",
                "title" : "search for [%s]" % query}        
        task_id, _ = self.tasksDb.save(task)
        print "Saved task ", task_id

        position = 0
        for result in results:
            i = SearchengineItem()
            i['title']= cleanText(result['title'])
            i['url'] = result['url']
            i['snippet'] = result['content']
            if i.is_valid():
                position += 1
                i['position'] = position
                docid, _ = self.itemsDb.save({"name" : i['url'], "value" : dict(i), "task_id" : task_id})
                print "Extracted ", i, "saved to", docid
        
        # Local result
#         localBlock = hxs.select("//div[@class='center-content']/div[@class='pois-panel']")
#         if localBlock:
#             print "Got local result"
#             divHtml = localBlock[0].extract()
#             docid, _ = self.itemsDb.save({"name" : "local_result", "value" : divHtml, "task_id" : task_id})
#             print "Extracted local result saved to", docid
            
