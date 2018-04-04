a = [1.0,2.0,3.0]
class Solution():
	def describe(self, a):
		n=len(a)
		if n==0:
			return [None,None,None,None]
		sum=0.000000
		ave=0.000000
		for i in a:
			sum+=i
		ave=sum/n
		
		if n==1:
			return [round(ave,6),None,0,-3]
		var=0.000000
		si=0.000000
		san=0.000000
		for i2 in a:
			var+=(i2-ave)**2
			si+=(i2-ave)**4
			san+=(i2-ave)**3
		svar=var/n
		var=var/(n-1)
		if var==0:
			return [round(ave,6),round(var,6),None,None]
		san=san/n
		skew=san/(svar**1.5)
		si=si/n
		kurt=si/(svar**2)
		return [round(ave,6),round(var,6),round(skew,6),round(kurt-3,6)]
p=Solution()
print p.describe(a)