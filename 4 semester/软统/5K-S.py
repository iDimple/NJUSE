class Solution():
    def ks_2samp(self, data1, data2):
            num1 = len(data1)
            num2 = len(data2)
            if(num1==0)|(num2==0):
                return None
            data1.sort()
            data2.sort()
            min1 = min(data1)
            max1 = max(data1)
            min2 = min(data2)
            max2 = max(data2)
            distance = (max2-min1)/num1
            
            minX = min(min1,min2)
            maxX = max(max1,max2)
            
            P = 0.0
            Pf1 = 0.0
            Pf2 = 0.0
            Pg1 = 0.0
            while(minX<maxX+distance):
                i=minX
                minX+=distance
                count1=self.choose(data1,i)
                count2=self.choose(data2,i)
                Pf1=count1*1.0/num1
                Pf2=count2*1.0/num2
                Pt =abs(Pf1-Pf2)
                P = max(P,Pt)
            return round(P,6)
    def choose(self,a,b):
        count=0
        for i in a:
            if i<b:
                count+=1
        return count