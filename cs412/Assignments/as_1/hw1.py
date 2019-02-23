import fileinput

#we make the transaction_list as a global bariable since it remains the same all the time.
transaction_list = []
minsup = 0

'''
#each transaction has a list of items
class transaction:

    def __init__(self,item_list):
        self.item_list = item_list
'''

def readfile():
    global minsup
    list = []
    for line in fileinput.input():
        for item in line:
            list.append(item)
    minsup = int(list[0])
    return(list)

def rm_minsup(list):
    list.pop(0)
    list.pop(0)
    return(list)

def rm_sapce(list):
    index = 0
    for item in list:
        if item == ' ':
            list.pop(index)
        index += 1
    return(list)

'''

def rm_sign(list):
    index = 0
    for item in list:
        if item == '\n':
            list.pop(index)
        index += 1
    return(list)

'''

#we want to make a list of transactions with each transaction being a list of item
def make_trans_set(list):
    global transaction_list
    item_list = []
    for item in list:
        if item != '\n':
            item_list.append(item)
        else:
            transaction_list.append(item_list)
            #print(item_list)
            item_list = []
    #when we finish looping through the last transaction, we have to
    #put it into our list manually since there is no \n at the end
    transaction_list.append(item_list)
    #print(item_list)
    return(transaction_list)

#sort the items in dictionariy from largest to smallest values
def sort_max_dic(dic):
    sorted_dic = sorted(dic, key = lambda x: (-dic[x], x))
    return(sorted_dic)

def check_frequency(dic):
    global minsup
    for item in dic:
        if dic[item] < minsup:
            dic.pop(item)
    return(dic)

def apriori():

    global minsup
    global transaction_list
    diff_indicator = 0
    #we want to store itemset in dictionaries, the values will be their count
    itemset = {}
    itemset_next = {}
    #we initiallize dictionariy itemset to be the 1-item set
    for tran in transaction_list:
        for item in tran:
            if item in itemset:
                itemset[item] += 1
            else:
                itemset[item] = 1
    #we chunk off those items with support less than minsup
    itemset = check_frequency(itemset)
    itemset = sort_max_dic(itemset)

    for itemlist_i in itemset:
        for itemlist_j in itemset:
            for element1 in itemlist_i:
                for element2 in itemlist_j:
                    if element1.equal(element2):
                        continue
                    else:
                        diff_indicator += 1
            if diff_indicator == 1:
                support_ij = int(itemset[itemlist_i])
                itemlist_new = list(set(itemlist_i + itemlist_j))
                itemlist_new.sort()
                itemset_next[itemlist_new] = support_ij

            #when we are done ,we have to reset the count to 0
            diff_indicator = 0

    #Now we sort the dic by the values and then by their aphabetical order.
    itemset_next = sort_max_dic(itemset_next)



def main():
    global minsup
    content_all = readfile()
    content_all = rm_minsup(content_all)
    content_all = rm_sapce(content_all)
    #print(content_all)


    result = make_trans_set(content_all)








if __name__== "__main__":
  main()
