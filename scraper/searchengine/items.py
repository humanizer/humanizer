# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/topics/items.html

from scrapy.item import Item, Field

class SearchengineItem(Item):
    # define the fields for your item here like:
    title = Field()
    url = Field()
    snippet = Field()
    position = Field()
    
    def is_valid(self):
        return self.get('title') and self.get('url') and self.get('snippet')
