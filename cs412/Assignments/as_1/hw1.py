import sys
#we make the transaction_list as a global bariable since it remains the same all the time.
transaction_list = []
minsup = 0

def readfile():
    global minsup
    list = []
    for line in sys.stdin:
        line_list = line.split()
        for item in line_list:
                list.append(item)
        list.append('\n')
    list.pop(-1)
    minsup = int(list[0])
    return(list)

def rm_minsup(list):
    global minsup
    minsup = int(list[0])
    #pop them minsup
    list.pop(0)
    #pop the \n sign
    list.pop(0)
    return(list)

#we want to make a list of transactions with each transaction being a list of item
def make_trans_set():
    global minsup
    global transaction_list
    item_list = []
    list = readfile()
    list = rm_minsup(list)

    for item in list:
        if item != '\n':
            item_list.append(item)
        else:
            transaction_list.append(item_list)
            item_list = []
    #when we finish looping through the last transaction, we have to
    #put it into our list manually since there is no \n at the end
    transaction_list.append(item_list)

def check_frequency(list_list):
    global minsup
    index = 0
    for item in list_list:
        if item[1] < minsup:
            list_list.pop(index)
        index = index + 1
    return(list_list)

def dic_to_lili(dic):
    list = []
    for key in dic:
        temp = []
        temp.append([key])
        temp.append(dic[key])
        list.append(temp)
    return(list)

def sort_lili_sup(list_list):
    list_list = sorted(list_list, key=lambda x: x[1], reverse = True)
    return(list_list)

def sort_lili_key(list_list):
    list_list = sorted(list_list, key=lambda x: x[0])
    return(list_list)

def remove_dup_lili(list_list):
    indicator = 0
    l = len(list_list)
    if l == 1 :
        return(list_list)
    for i in range(l-1):
        temp = list_list[0]
        list_list.pop(0)
        for check_dup in list_list:
            if set(temp[0]) == set(check_dup[0]):
                indicator = 1
        if indicator == 0:
            list_list.append(temp)
        indicator = 0
    return(list_list)

def remove_lesup(list_list):
    global minsup
    #print(list_list)
    l = len(list_list)
    for i in range(l-1):
        indicator = 0
        temp = list_list[0]
        list_list.pop(0)
        if temp[1] < minsup:
                indicator = 1
        if indicator == 0:
            list_list.append(temp)
    return(list_list)

def add_support(list_list):
    global transaction_list
    for tran in transaction_list:
        #we use a indicator to see whether all of item in a set are in a tran or not
        indicator_exist = 1
        for itemlist in list_list:
            indicator_exist = 1
            for element in itemlist[0]:
                if element in tran:
                    continue
                else:
                    indicator_exist = 0
            #if all elements are in a same transaction, we add one to the support
            if indicator_exist == 1:
                itemlist[1] = itemlist[1] + 1
    return(list_list)

def apriori():
    global minsup
    global transaction_list
    diff_indicator = 0
    result_list = []
    #later we will convert it into a list of tuples
    itemset = {}
    #we initiallize dictionariy itemset to be the 1-item set
    for tran in transaction_list:
        for item in tran:
            if item in itemset:
                itemset[item] += 1
            else:
                itemset[item] = 1
    #we convert the dictionary to a list of lists, where each sublist
    #has two elements, first one is the item, second one is the support
    itemset = dic_to_lili(itemset)
    #we chunk off those items with support less than minsup
    itemset = check_frequency(itemset)
    #we sort the list of lists by item supports
    itemset = sort_lili_sup(itemset)

    list_this_step = itemset
    while(len(list_this_step) > 1):
        list_generate = []
        for itemlist_i in list_this_step:
            for itemlist_j in list_this_step:
                if set(itemlist_i[0]) == set(itemlist_j[0]):
                    continue
                else:
                    #we initialize a new copy of it in order to make some changes
                    temp_2list = []
                    #we cant just pass reference
                    for temp in itemlist_i[0]:
                        temp_2list.append(temp)
                    #now we want to generate all possible candidates
                    for element2 in itemlist_j[0]:
                        if element2 not in itemlist_i[0]:
                            temp_2list.append(element2)
                    temp_lili = [temp_2list,0]
                    list_generate.append(temp_lili)
        #now we want to remove those dupilicates sublists useing our function
        list_generate = remove_dup_lili(list_generate)
        #now we want to clarify their support
        list_generate = add_support(list_generate)
        #we remove those set with support less than min support
        list_next_step = remove_lesup(list_generate)
        #we append the list of list in this step to our result
        result_list = result_list + list_this_step
        #move to next step
        list_this_step = list_next_step
    if len(list_this_step) == 1:
        result_list = result_list + list_this_step
    #since in the last step, these functions in the while loop wont get called
    #or saying we just want to secure our result to be valid
    result_list = remove_dup_lili(result_list)
    result_list = remove_lesup(result_list)
    return(result_list)

def lets_print(list_list):
    global minsup
    inner_lili = []
    print_lili = []
    list_list = sort_lili_sup(list_list)
    for item_sup_list in list_list:
        temp_list = []
        item_string = " ".join(sorted(item_sup_list[0]))
        temp_list.append(item_string)
        temp_list.append(item_sup_list[1])
        inner_lili.append(temp_list)
    #now we want to split them and then sort then seperately
    #first we want to know what is the largest supports
    maxsup = minsup
    for item in inner_lili:
        if item[1] > maxsup:
            maxsup = item[1]
    #we want to sort those items with the same support alpabetically
    while(maxsup >= minsup):
        sort_sublist = []
        for item_sup_list in inner_lili:
            if item_sup_list[1] == maxsup:
                sort_sublist.append(item_sup_list)
        print_lili = print_lili + sorted(sort_sublist)
        maxsup = maxsup - 1

    for item in print_lili:
        print(item[1],'[', end='',)
        print(item[0], end='',)
        print(']')

def give_closed(list_list):
    if len(list_list) == 1:
        return(list_list)
    final = []
    for item in list_list:
        same_sup_list = []
        for other in list_list:
            if set(item[0]) != set(other[0]):
                if item[1] == other[1]:
                    same_sup_list.append(other)
        if same_sup_list == []:
            final.append(item)
        else:
            for other in same_sup_list:
                indicator = 1
                for word in item[0]:
                    if word in other[0]:
                        continue
                    else:
                        indicator = 0
                        break
                if indicator == 1:
                    break
            if indicator == 0:
                final.append(item)
    return(final)

def give_max(list_list):
    if len(list_list) == 1:
        return(list_list)
    final = []
    for item in list_list:
        for other in list_list:
            if set(item[0]) != set(other[0]):
                indicator = 1
                for word in item[0]:
                    if word in other[0]:
                        continue
                    else:
                        indicator = 0
                        break
                if indicator == 1:
                        break
                if item == list_list[-1]:
                    if other == list_list[-2]:
                        if indicator == 0:
                            final.append(item)
                if item != list_list[-1]:
                    if other == list_list[-1]:
                        if indicator == 0:
                            final.append(item)
    return(final)

def main():
    global minsup
    global transaction_list
    make_trans_set()

    result_list = apriori()
    result_list = sort_lili_sup(result_list)
    lets_print(result_list)
    print()

    final_closed = give_closed(result_list)
    final_closed = sort_lili_sup(final_closed)
    lets_print(final_closed)
    print()

    final_max = give_max(result_list)
    final_max = sort_lili_sup(final_max)
    lets_print(final_max)


if __name__== "__main__":
  main()
