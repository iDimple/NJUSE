from scipy.stats import t as T
class Solution():
    def ttest_1samp(self, a, popmean):
        if(len(a)==0):
            return [None,None]
        mean=sum(a)/float(len(a))
        t=0.0
        s2=0.0
        s=0.0
        for i in range(len(a)):
            s2+=(a[i]-mean)**2
        s2/=(len(a)-1)
        s=s2**0.5
        t=(mean-popmean)/(s/len(a)**0.5)
        p=T.sf(t,len(a)-1)
        if p>0.5:
            p = 1-p
        else:
            p=2*p
        return [round(t,6),round(p,6)]
    
x=Solution()
print x.ttest_1samp([1.0,2.0,3.0],2.0)