#!/usr/bin/env python
# encoding: utf-8

from bs4 import BeautifulSoup
import couchdb
from scrapy.selector import HtmlXPathSelector
from scrapy.spider import BaseSpider
import urllib

from searchengine.items import SearchengineItem

QUERIES = ['free movies',
           'quán bia ngon rẻ',
           'bia hơi hà nội']
#QUERIES = ['entertainment']

LANGUAGE = "vi"
LOCALE = "vn"

def ConvertQueryToUrl(query):
    return 'http://www.google.com/search?q=' + urllib.quote_plus(query) + '&hl=%s&gl=%s' % (LANGUAGE, LOCALE)

def ConvertUrlToQuery(url):
    url = url[len('http://www.google.com/search?q='):]
    query = url[0:-len('&hl=vi&gl=vn')]
    return urllib.unquote_plus(query)

def cleanText(data):
    if type(data) == list:
        return map(cleanText, data)
    elif type(data) in [str, unicode] :
        # return lxml.html.fromstring(data).text_content()
        return ' '.join(BeautifulSoup(data).findAll(text=True))
    else:
        return data

RATER = "kimcuong@gmail.com"
PROJECT_ID = "49abdb0279e6d16429b4cb3624001736"

class GoogleSpider(BaseSpider):
    name = 'google'
    allowed_domains = ['google.com']
    start_urls = []
    couch = None
    
    def __init__(self):
        GoogleSpider.start_urls = map(ConvertQueryToUrl, QUERIES)
        GoogleSpider.couch = couchdb.Server("http://humanizer:123456@humanizer.iriscouch.com")
        self.tasksDb = GoogleSpider.couch["tasks"]
        self.itemsDb = GoogleSpider.couch["items"]

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
                "project_id" : PROJECT_ID,
                "raters" : [RATER],
                "status" : {RATER : "new"},
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
        
        #import pdb;pdb.set_trace()
