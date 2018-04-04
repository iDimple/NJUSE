import urllib2
import string
import math
from scipy.stats import norm as N
class Solution():
    def solve(self):
        strHtml = urllib2.urlopen('http://112.124.1.3:8050/getData/101').read()
        list=[]
        sum=0.0
        length=0
        mean=0.0
        low=0.0
        high=0.0
        x=strHtml.split("[")
        for i in range(len(x)-2):
            list.append(string.atoi(x[i+2].split(', ')[2]))
        length=len(list)
        for j in range(len(list)):
            if(list[j]>5 and list[j]<=10):
                sum+=list[j]*4.33
            elif(list[j]>25 and list[j]<49):
                sum+=list[j]
            else:
                length-=1
        mean=sum/float(length)
        low=mean-4/math.sqrt(length)*(-N.ppf(0.025))
        high=mean+4/math.sqrt(length)*(-N.ppf(0.025))
        return [low,high]
    
