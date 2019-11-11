import Moter
import time

m=Moter.Moter(17,18,27,22)
m.forword()
time.sleep(1)
m.left()
time.sleep(1)
m.right()
time.sleep(1)
m.back()
time.sleep(1)
m.stop()

