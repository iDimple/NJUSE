from scipy.stats import chi2
class Solution():
    def independence_test(self, A):
        if(len(A)==1):
            return [0.0,None]
        elif(len(A[0])==1):
            return [0.0,None]
        else:
            r=len(A)
            c=len(A[0])
            ni=[]
            nj=[]
            n=0.0
            C=0.0
            for i in A:
                ni.append(sum(i))
            for i in A[0]:
                nj.append(0)
            for i in range(r):
                for j in range(c):
                    nj[j]+=A[i][j]
                    n+=A[i][j]
            for i in range(r):
                for j in range(c):
                    C+=(A[i][j]-ni[i]*nj[j]/n)**2/(ni[i]*nj[j]/n)
            P=chi2.sf(C,(r-1)*(c-1))
            return [round(C,6),round(P,6)]