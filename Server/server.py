from datetime import date
import tornado.escape
import tornado.ioloop
import tornado.web
import operator
import sys
from math import radians, cos, sin, asin, sqrt
import MySQLdb
 
class VersionHandler(tornado.web.RequestHandler):
    def get(self):
        response = { 'version': '0.1',
                     'last_build':  date.today().isoformat() }
        self.write(response)

class ParkingRequestHandler(tornado.web.RequestHandler):
    def post(self):
        try:
            data = tornado.escape.json_decode(self.request.body) 
            #self.write(str(type(data)))
            longitude = data['long']
            latitude = data['lat']
            city = data['city']
            option = data['option']
            hour = data['hour']
            minutes = data['minutes']
            bus = Bus()
            closer_station = bus.getCloserCityStation(city,longitude,latitude)
            closer_parking = bus.getCloserParking(city,longitude,latitude)
            closer_parking_gps = bus.getParkingGPS(closer_parking[0])
            closer_parking_latitude = closer_parking_gps[1]
            closer_parking_longitude = closer_parking_gps[0]
            closer_parking_station = bus.getCloserCityStation(city,closer_parking_longitude,closer_parking_latitude)
            #self.write(str(closer_parking_gps))
            #self.write("The closer station is: "+str(closer_station))
            #self.write("The closer parking is: "+str(closer_parking))
            returnJson="{\"parking\":{\"name\":\"%s\",\"distancem\":\"%sm\",\"arret\":\"%s\"},\"arret\":{\"name\":\"%s\",\"distancem\":\"%sm\"}}"%(closer_parking[0],closer_parking[1]*1000,closer_parking_station[0],closer_station[0],closer_station[1]*1000)
            self.write(returnJson)
        except:
            self.clear()
            self.set_status(400)
            self.finish("<html><body>"+str(sys.exc_info())+"</body></html>")

class Bus:
    
    def __init__ (self):
        # DB connection
        self.db=MySQLdb.connect(host="localhost",user="root", passwd="",db="Tec")
        self.c=self.db.cursor()

    def getParkingGPS(self, parkingName):
        sql = "SELECT nom, longitude, latitude FROM Tec.Parking where nom = \"%s\";"%(parkingName);
        print sql
        self.c.execute(sql)
        rows = self.c.fetchall()
        for row in rows:
            return (row[1],row[2])

    def getCloserParking(self, city, longitude, latitude):
        sql = "SELECT nom, longitude, latitude FROM Tec.Parking;"
        self.c.execute(sql)
        parkingDict={}
        rows = self.c.fetchall()
        for row in rows:
            parkingDict[row[0]]=self._stationToPoint(float(row[1]),float(row[2]),float(longitude),float(latitude))
        parkingDict_sorted = sorted(parkingDict.iteritems(),key=operator.itemgetter(1))
        return parkingDict_sorted[0]
        
    def getCloserCityStation(self, city, longitude, latitude):
        sql="SELECT stop_name, stop_lat, stop_lon FROM Tec.stops_gps where stop_name like '%NAMUR%' OR stop_name like '%BOUGE%' OR stop_name like '%BELGRADE%' OR stop_name like '%FLAWINNE%' OR stop_name like '%JAMBES%' OR stop_name like '%LA PLANTE%' OR stop_name like '%MALONNE%' OR stop_name like '%SAINT-SERVAIS%';"
        self.c.execute(sql)
        stationsDict={}
        rows = self.c.fetchall()
        for row in rows:
            stationsDict[row[0]]=self._stationToPoint(float(row[2]),float(row[1]),float(longitude),float(latitude))
        stationDict_sorted = sorted(stationsDict.iteritems(),key=operator.itemgetter(1))
        #print stationDict_sorted
        return stationDict_sorted[0]
        

    def _stationToPoint(self,lon1, lat1, lon2, lat2):
        """
        Calculate the great circle distance between two points 
        on the earth (specified in decimal degrees)
        cf: http://stackoverflow.com/questions/4913349/haversine-formula-in-python-bearing-and-distance-between-two-gps-points
        """
        # convert decimal degrees to radians 
        lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])

        # haversine formula 
        dlon = lon2 - lon1 
        dlat = lat2 - lat1 
        a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
        c = 2 * asin(sqrt(a)) 

        # 6367 km is the radius of the Earth
        km = 6367 * c
        return km
                
 
application = tornado.web.Application([
    (r"/version", VersionHandler),
    (r"/parkingRequest", ParkingRequestHandler)
])
 
if __name__ == "__main__":
    application.listen(8888)
    tornado.ioloop.IOLoop.instance().start()
