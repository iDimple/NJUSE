from math import pi
from math import exp
import random
class Solution:
    def solve(self,a,b):
        n = 1000000
        k = 0
        if (a>b):
            return None
        if (a==b):
            return 0
        else:
            largest = exp(-a**2/2)/(2*pi)**0.5
            for i in range(n):
                x = random.uniform(a,b)
                y = random.uniform(0,largest)
                X = exp(-x**2/2)/(2*pi)**0.5
                if X >= y:
                    k = k + 1
            m = (b-a)*largest*k/n
            m = round(m,6)
            return m
if __name__ =="__main__":
    solution = Solution()
    print solution.solve(1,2)
