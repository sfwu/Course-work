import numpy as np

'''
 First question:
 Find first attribute according to information gain
'''


'''
Note: because 'Info(D)' is the same, so you only need to find the minimum info_A）(D)
这里就是对于一个属性以下不同的类型的 数据 数一下 yes 然后再数一下no
'''
I_pair = [ 4, 1]

'''
手动输入total 这个是一共有多少条数据!!!!!!!!
'''
totalnum =     10


sum_of_I_pair = I_pair[0] + I_pair[1]

p1 = float(I_pair[0]/sum_of_I_pair)
p2 = float(I_pair[1]/sum_of_I_pair)

Info_d_i=-p1*np.log2(p1) - p2*np.log2(p2)

info_d_i_with_ratio = float(sum_of_I_pair/totalnum)*Info_d_i

print('')
print('')

print('Info_d_i=',Info_d_i)
print('info_d_i_with_ratio=',info_d_i_with_ratio)


print('')
print('=======================Question2 Cut line =========================')
print('')


'''
 Second question:
 Find first attribute according to gini index

根据一个attribute分完之后 数一下每个部分对应的yes 数量 和no的数量
'''
pair = [  4, 1 ]

'''
手动输入total 这个是一共有多少条数据!!!!!!!!
'''
total =     10

sum_of_pair = pair[0] + pair[1]

p11 = float(pair[0]/sum_of_pair)
p22 = float(pair[1]/sum_of_pair)

gini_d_i= 1.0 - p11*p11 - p22*p22

gini_d_i_with_ratio = float(sum_of_pair/total)*gini_d_i

print('gini_d_i=',gini_d_i)
print('gini_d_i_with_ratio=',gini_d_i_with_ratio)

print('')
print('=======================Question3 Cut line =========================')
print('')

'''
 Third question:
 Naive Bayes
'''

'''
满足一个条件的情况下 算一些有几个yes 几个no 每个pair先输入yes 再输入no
'''
li = [ [   2  ,   3  ]   ,  [   4   ,   2   ]  , [  6    ,   1   ]   , [   6    ,   2   ] ]

'''
数一下整个的dataset 手动输入一共有多少个yes 一共有多少个no
'''
total_yes = 9
total_no = 5



p_given_yes = 1.0
p_given_no = 1.0

for sublist in li:
    p_given_yes = p_given_yes*(sublist[0]/total_yes)
    print('partial probibility for yes =',sublist[0]/total_yes)
    p_given_no = p_given_no*(sublist[1]/total_no)
    print('partial probibility for no =', sublist[1]/total_no)

print('----------------------------------------------')
print('whole probability given yes =', p_given_yes)
print('whole probability given no =', p_given_no)

print('--------the following is the finall result we want to compare---------')
print('final probability given yes =', p_given_yes*(total_yes/(total_yes+total_no)))
print('final probability given no =', p_given_no*(total_no/(total_yes+total_no)))
