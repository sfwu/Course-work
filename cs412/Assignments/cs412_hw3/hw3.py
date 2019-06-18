import sys
import math

#3-dimensions list. each list_1 is an element, list_2 contaisn its attributes.
#revised

def readfile():
    list_train = []
    list_test = []
    t_s_indicator = 0
    round_indicator = 0
    for line in sys.stdin:
        line_list = line.split()
        ele_list = []
        attr_dic = {}
        temp_label = int(line_list[0])
        if temp_label == 0:
            t_s_indicator = 1

        for item in line_list:
            temp_list = item.split(':')
            if len(temp_list) == 1:
                ele_list.append(int(temp_list[0]))
            else:
                attr_dic[int(temp_list[0])] = float(temp_list[1])

        ele_list.append(attr_dic)
        attr_dic = {}
        if t_s_indicator == 0:
            list_train.append(ele_list)
        else:
            list_test.append(ele_list)
    return(list_train,list_test)

#this part is good
def sort_li3(li3,attr):
    li3 = sorted(li3, key=lambda x: x[1][attr])
    return(li3)

#this part is good
def get_label(count_dic):
    count = -1
    for label in count_dic:
        if count < count_dic[label]:
            wantlabel = label
            count = count_dic[label]
        elif count == count_dic[label]:
            if label < wantlabel:
                wantlabel = label
            else:
                continue
        else:
            continue
    return(wantlabel)

#this part is good
def gini_D(list_k):
    keycountdic = count_key(list_k)
    totalnum = float(len(list_k))
    giniD = 1.0
    for key in keycountdic:
        giniD = giniD - (keycountdic[key]/totalnum)*(keycountdic[key]/totalnum)
    return(giniD)

#this part is good
def gini_AD(list1,list2):
    len1 = len(list1)
    len2 = len(list2)
    totalnum = len1 + len2
    giniAD = float((len1/totalnum)*gini_D(list1) + (len2/totalnum)*gini_D(list2))
    return(giniAD)

#revised/good
#find giniindex and proper thh for an attribute

def gini_index_attr(list_k,attr):

    list_k = sort_li3(list_k,attr)

    min_gini = 100000000000000
    good_thh = 100000000000000
    margin = 0.0
    good_margin = -1.0

    round_indicator = 0
    this_thh = 0
    last_thh = 0

    for i in range(len(list_k)-1):
        giniD1 = 1.0
        giniD2 = 1.0
        giniIndex = 0.0
        if list_k[i][1][attr] != list_k[i+1][1][attr]:
            list1 = []
            list2 = []
            this_thh = ((list_k[i][1][attr] + list_k[i+1][1][attr]))/2
            margin = abs(list_k[i+1][1][attr] - (list_k[i][1][attr]))

            if round_indicator == 0:
                round_indicator = 1

                list1 = list_k[:i]
                list2 = list_k[i+1:-1]

                count1 = i + 1
                count2 = len(list_k) - i - 1

                keycountdic1 = count_key(list1)
                keycountdic2 = count_key(list2)

                for key in keycountdic1:
                    giniD1 = giniD1 - (keycountdic1[key]/count1)*(keycountdic1[key]/count1)
                for key in keycountdic2:
                    giniD2 = giniD2 - (keycountdic2[key]/count2)*(keycountdic2[key]/count2)

                totalnum = count1 + count2
                giniIndex = float((count1/totalnum)*giniD1 + (count2/totalnum)*giniD2)

                if giniIndex < min_gini:
                    min_gini = giniIndex
                    good_thh = this_thh
                    good_margin = margin
                if giniIndex == min_gini:
                    if margin > good_margin:
                        good_thh = this_thh
                    if margin == good_margin:
                        if this_thh < good_thh:
                            good_thh = this_thh
            else:
                for j in range(i-last_i):
                    count1 = count1 + 1
                    count2 = count2 - 1
                    keycountdic2[list_k[last_i+j+1][0]] = keycountdic2[list_k[last_i+j+1][0]] - 1
                    if list_k[last_i+j+1][0] in keycountdic1:
                        keycountdic1[list_k[last_i+j+1][0]] = keycountdic1[list_k[last_i+j+1][0]] + 1
                    else:
                        keycountdic1[list_k[last_i+j+1][0]] = 1

                for key in keycountdic1:
                    giniD1 = giniD1 - (keycountdic1[key]/count1)*(keycountdic1[key]/count1)
                for key in keycountdic2:
                    giniD2 = giniD2 - (keycountdic2[key]/count2)*(keycountdic2[key]/count2)

                giniIndex = float((count1/totalnum)*giniD1 + (count2/totalnum)*giniD2)

                if giniIndex < min_gini:
                    min_gini = giniIndex
                    good_thh = this_thh
                    good_margin = margin
                if giniIndex == min_gini:
                    if margin > good_margin:
                        good_thh = this_thh
                    if margin == good_margin:
                        if this_thh < good_thh:
                            good_thh = this_thh

        last_i = i
    return(min_gini,good_thh)

#this part is good
def find_right_attr(list_k):
    good_attr = 100000000000000
    good_attr_thh = 100000000000000
    min_gini_attr = 100000000000000
    for attr in list_k[0][1]:
        (attr_gini,attr_thh) = gini_index_attr(list_k,attr)
        if attr_gini < min_gini_attr:
            min_gini_attr = attr_gini
            good_attr = attr
            good_attr_thh = attr_thh
        elif attr_gini == min_gini_attr:
            if attr < good_attr:
                good_attr = attr
                good_attr_thh = attr_thh
            else:
                continue
        else:
            continue
    return(good_attr,good_attr_thh)


#this part is good
def count_key(list_k):
    keycountdic = {}
    for element in list_k:
        if element[0] in keycountdic:
            keycountdic[element[0]] = keycountdic[element[0]] + 1
        else:
            keycountdic[element[0]] = 1
    return(keycountdic)

#revised
def predict_label_tree(list_train,list_test):
    (good_attr,good_attr_thh) = find_right_attr(list_train)
    list_left,list_right,li1,li2,li3,li4,result = [],[],[],[],[],[],[]

    keycountdic_0 = count_key(list_train)
    if len(keycountdic_0) == 1:
        label_0 = next(iter(keycountdic_0))
        for test_element in list_test:
            result.append(label_0)
        return(result)

    else:
        for element in list_train:
            if element[1][good_attr] > good_attr_thh:
                list_right.append(element)
            else:
                list_left.append(element)

        keycountdic_l = count_key(list_left)
        if len(keycountdic_l) == 1:
            (good_attr_l,good_attr_thh_l) = find_right_attr(list_left)
            label_1 = next(iter(keycountdic_l))
            label_2 = label_1
        if len(keycountdic_l) != 1:
            (good_attr_l,good_attr_thh_l) = find_right_attr(list_left)
            for element in list_left:
                if element[1][good_attr_l] > good_attr_thh_l:
                    li2.append(element)
                else:
                    li1.append(element)

            keycountdic_1 = count_key(li1)
            keycountdic_2 = count_key(li2)
            label_1 = get_label(keycountdic_1)
            label_2 = get_label(keycountdic_2)

        keycountdic_r = count_key(list_right)
        if len(keycountdic_r) == 1:
            (good_attr_r,good_attr_thh_r) = find_right_attr(list_right)
            label_3 = next(iter(keycountdic_r))
            label_4 = label_3
        if len(keycountdic_r) != 1:
            (good_attr_r,good_attr_thh_r) = find_right_attr(list_right)
            for element in list_right:
                if element[1][good_attr_r] > good_attr_thh_r:
                    li4.append(element)
                else:
                    li3.append(element)
            keycountdic_3 = count_key(li3)
            keycountdic_4 = count_key(li4)
            label_3 = get_label(keycountdic_3)
            label_4 = get_label(keycountdic_4)

        for test_element in list_test:
            if test_element[1][good_attr] > good_attr_thh:
                if test_element[1][good_attr_r] > good_attr_thh_r:
                    result.append(label_4)
                else:
                    result.append(label_3)
            else:
                if test_element[1][good_attr_l] > good_attr_thh_l:
                    result.append(label_2)
                else:
                    result.append(label_1)

    return(result)
#revised
def distance(element1,element2):
    temp = 0.0
    for attr in element1[1]:
        temp = temp + (element2[1][attr]-element1[1][attr])*(element2[1][attr]-element1[1][attr])
    d = math.sqrt(temp)
    return(d)

def k_neighbour(list_train,element):
    label_li = []
    newlist = []
    for neighbour in list_train:
        temp_distance = distance(neighbour,element)
        label_li.append([temp_distance,neighbour[0]])
        label_li.sort()
    newlist.append(label_li[0])
    newlist.append(label_li[1])
    newlist.append(label_li[2])
    return(newlist)

def count_key_knn(list_k):
    keycountdic = {}
    for element in list_k:
        if element[1] in keycountdic:
            keycountdic[element[1]] = keycountdic[element[1]] + 1
        else:
            keycountdic[element[1]] = 1
    return(keycountdic)

def getlabel(al):
    dic = count_key_knn(al)
    if len(dic)==1:
        return -1
    elif len(dic)==2:
        for label in dic:
            if dic[label]==2:
                return(label)
    else:
        return(min(dic.keys()))

def main():
    (list_train,list_test) = readfile()
    prediction_tree = predict_label_tree(list_train,list_test)
    for test in prediction_tree:
        print(test)
    '''
    print('')
    for test in list_test:
        newlist = k_neighbour(list_train,test)
        wantlabel = getlabel(newlist)
        if wantlabel == -1:
            print(newlist[0][1])
        else:
            print(wantlabel)
    '''
if __name__== "__main__":
  main()
