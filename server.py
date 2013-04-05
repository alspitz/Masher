#!/usr/bin/python
'''
Server for Songmash
This server streams songs to clients
Alex Spitzer 2013
'''

import tornado.httpserver
import tornado.ioloop
import tornado.web

PORT = 8080

class MainHandler(tornado.web.RequestHandler):
    def get(self):
        print "get w/o path"
        self.write("Tornado Server SONGMASH")

    def get(self, path):
        addr = self.request.remote_ip
        if not path:
            path = "index.html"

        print "GET for %s by address: %s" % (path, addr)
        
        if path.endswith(".html"):
            self.set_header("Content-Type", "text/html")
            f = open(path, 'r')
            self.write(f.read())
            f.close()

        if path.endswith(".mp3"):
            self.set_header("Content-Type", "audio/mp3")
            self.write("Content-Type: audio/mp3\n\n")
            f = open(path, 'rb')
            self.write(f.read())
            f.close()

class Mp3Handler(tornado.web.RequestHandler):
    pass


application = tornado.web.Application([
    (r"/(.*)", MainHandler),
])

if __name__ == "__main__":
    http_server = tornado.httpserver.HTTPServer(application)
    http_server.listen(PORT)
    tornado.ioloop.IOLoop.instance().start()
