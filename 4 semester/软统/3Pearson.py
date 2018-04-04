from scipy.stats import t as T

class Solution():
    def pearsonr(self, x, y):
        sx =0.0
        sy =0.0
        sxy =0.0
        sxx =0.0
        syy = 0.0
        if len(x)==0 or len(y)==0:
            return [None,None]
        if len(x)!=len(y):
            return [None,None]
        n = len(x)
        for i in range(0,n):
            sx+=x[i]
            sy+=y[i]
            sxy+=x[i]*y[i]
            sxx+=x[i]**2
            syy+=y[i]**2
        rxy = (n*sxy-sx*sy)/((n*sxx-sx**2)*(n*syy-sy**2))**0.5
        v = (1-rxy**2)
        if v==0:
            return [round(rxy,6),0.000000]
        t = rxy*(((n-2)/(1-rxy**2))**0.5)
        p = T.sf(t,n-2)
        if p>0.5:
            p = 1-p
        else:
            p=2*p
        return [round(rxy,6),round(p,6)]

p=Solution()
print p.pearsonr([1.0,2.0,3.0],[2.0,2.0,3.0])

