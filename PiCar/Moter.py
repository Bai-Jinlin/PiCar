import RPi.GPIO as GPIO
class Moter(object):
    def __init__(self,A1,A2,B1,B2):
        self.A1=A1
        self.A2=A2
        self.B1=B1
        self.B2=B2
        self.pins=[A1,A2,B1,B2]
        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)
        for pin in self.pins:
            GPIO.setup(pin,GPIO.OUT)
            GPIO.output(pin,GPIO.LOW)

    def cleanpin(self):
        for pin in self.pins:
            GPIO.output(pin,GPIO.LOW)

    def forword(self):
        self.cleanpin()
        GPIO.output(self.A1,GPIO.HIGH)
        GPIO.output(self.B2,GPIO.HIGH)

    def back(self):
        self.cleanpin()
        GPIO.output(self.A2,GPIO.HIGH)
        GPIO.output(self.B1,GPIO.HIGH)


    def left(self):
        self.cleanpin()
        GPIO.output(self.A1,GPIO.HIGH)

    def right(self):
        self.cleanpin()
        GPIO.output(self.B2,GPIO.HIGH)

    def stop(self):
        self.cleanpin()
