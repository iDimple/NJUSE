from scipy.stats import f
class Solution():
    def f_oneway(self, *args):
        if(len(args)==0):
            return [None,None]
        else:
            m = len(args)
            for i in range(0,m):#左开右闭
                if len(args[i])==0:
                    return [None,None]
            r = len(args[0])
            xi = []
            xim = []
            sumx = 0.0
            for i in range(0,m):
                xt = 0.0
                for j in range(0,r):
                    xt+=args[i][j]#一行,即某一因素的总合
                sumx+=xt
                xi.append(xt)
                xim.append(xt/r)#某一因素的平均
            xm = sumx/(m*r)#总的平均
            ST = 0.0
            Se = 0.0
            SA = 0.0
            for i in range(0,m):
                SA += (xim[i]-xm)**2
                for j in range(0,r):
                    Se+=(args[i][j]-xim[i])**2
                    ST+=(args[i][j]-xm)**2
            SA*=r
            fA = m-1
            fe = r*m-m
            VA = SA/fA
            Ve = Se/fe
            F = VA/Ve
            P = f.sf(F,fA,fe)
            return [round(F,6),round(P,6)]

        
p=Solution()
print p.f_oneway([1.0,2.0,3.0],[2.0,2.0,3.0])
print f.sf(6.4231,5-1,10-5)
