import sys
import math

#we will use this later
real_attr_list = []
#3-dimensions list. each list_1 is an element, list_2 contaisn its attributes.
def readfile():
    global real_attr_list
    list_train = []
    list_test = []
    t_s_indicator = 0
    round_indicator = 0
    for line in sys.stdin:
        line_list = line.split()
        ele_list = []
        attr_list = []

        temp_label = int(line[0])
        if temp_label == 0:
            t_s_indicator = 1

        for item in line_list:
            temp_list = item.split(':')
            if t_s_indicator == 0:
                if len(temp_list) == 1:
                    ele_list.append(temp_list[0])
                else:
                    attr_list.append(temp_list[1])
                    if round_indicator == 0:
                        real_attr_list.append(temp_list[0])
            else:
                if len(temp_list) == 1:
                    ele_list.append(temp_list[0])
                else:
                    attr_list.append(temp_list[1])
        round_indicator = 1
        ele_list.append(attr_list)
        attr_list = []
        if t_s_indicator == 0:
            list_train.append(ele_list)
        else:
            list_test.append(ele_list)
    return(list_train,list_test)

def sort_li3(li3,i):
    li3 = sorted(li3, key=lambda x: x[1][i], reverse = True)
    return(li3)

def list_dic(list_train):
    dic = {}
    for i in range(len(list_train)):
        dic[list_train[i][0]] = list_train[i][1]
    return(dic)

def count_key(dic):
    keycountdic = {}
    for key in dic:
        if key in keycountdic:
            keycountdic[key] = keycountdic[key] + 1
        else:
            keycountdic[key] = 1
    return(keycountdic)

def gini_D(list_k):
    keycountdic = count_key(list_dic(list_k))
    totalnum = len(list_k)
    giniD = 1
    for key in keycountdic:
        giniD = giniD - (keycountdic[key]/totalnum)*(keycountdic[key]/totalnum)
    return(giniD)

def gini_AD(list1,list2):
    totalnum = int(len(list1) + len(list2))
    giniAD = float((len(list1)/totalnum)*gini_D(list1) + (len(list2)/totalnum)*gini_D(list2))
    return(giniAD)

def get_label(count_dic):
    wantlabel = 0
    count = 0
    indicator = 0
    for label in count_dic:
        if indicator == 0:
            wantlabel = label
            count = count_dic[label]
            indicator = 1
        else:
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

def gini_index_attr(list_train,attr):

    round_indicator = 0
    totalnum = len(list_train)
    list_train = sort_li3(list_train,attr)

    list1 = []
    list2 = []

    min_gini = 0
    good_thh = 0.0

    for i in range(len(list_train)-1):
        thh = float(float(list_train[i][1][attr])/2 + float(list_train[i+1][1][attr])/2)

        for j in range(len(list_train)):
            if float(list_train[j][1][attr]) > thh:
                list2.append(list_train[j])
            else:
                list1.append(list_train[j])
        giniIndex = float(gini_D(list_train)) - float(gini_AD(list1,list2))

        if round_indicator == 0:
            min_gini = giniIndex
            good_thh = thh
            round_indicator = 1
        else:
            if giniIndex < min_gini:
                min_gini = giniIndex
                good_thh = thh
            elif giniIndex == min_gini:
                if thh < good_thh:
                    good_thh = thh
                else:
                    continue
    return(min_gini,good_thh)

def find_right_attr(list_train):
    global real_attr_list
    good_attr = 0
    min_gini_attr = 0
    round_indicator = 0
    for attr in range(len(list_train[0][1])):
        (attr_gini,attr_thh) = gini_index_attr(list_train,attr)
        if round_indicator == 0:
            round_indicator = 1
            min_gini_attr = attr_gini
            good_attr = attr
            good_attr_thh = attr_thh
        else:
            if attr_gini < min_gini_attr:
                min_gini_attr = attr_gini
                good_attr = attr
                good_attr_thh = attr_thh
            elif attr_gini == min_gini_attr:
                if real_attr_list[attr] < real_attr_list[good_attr]:
                    good_attr = attr
                    good_attr_thh = attr_thh
                else:
                    continue
            else:
                continue
    return(good_attr,good_attr_thh)

def predict_label_tree(list_train,list_test):
    (good_attr,good_attr_thh) = find_right_attr(list_train)
    list_left = []
    list_right = []
    dic1 = {}
    dic2 = {}
    dic3 = {}
    dic4 = {}
    for element in list_train:
        if float(element[1][good_attr]) > good_attr_thh:
            list_right.append(element)
        else:
            list_left.append(element)

    (good_attr_l,good_attr_thh_l) = find_right_attr(list_left)
    for element in list_left:
        if float(element[1][good_attr_l]) > good_attr_thh_l:
            dic2[element[0]] = element[1]
        else:
            dic1[element[0]] = element[1]

    (good_attr_r,good_attr_thh_r) = find_right_attr(list_right)
    for element in list_right:
        if float(element[1][good_attr_r]) > good_attr_thh_r:
            dic4[element[0]] = element[1]
        else:
            dic3[element[0]] = element[1]

    keycountdic_1 = count_key(dic1)
    keycountdic_2 = count_key(dic2)
    keycountdic_3 = count_key(dic3)
    keycountdic_4 = count_key(dic4)

    label_1 = get_label(keycountdic_1)
    label_2 = get_label(keycountdic_2)
    label_3 = get_label(keycountdic_3)
    label_4 = get_label(keycountdic_4)

    if not dic1:
        label_1 = label_2
    if not dic2:
        label_2 = label_1
    if not dic3:
        label_3 = label_4
    if not dic4:
        label_4 = label_3

    for test_element in list_test:
        if float(test_element[1][good_attr]) > good_attr_thh:
            if float(test_element[1][good_attr_r]) > good_attr_r:
                test_element[0] = label_4
            else:
                test_element[0] = label_3
        else:
            if float(test_element[1][good_attr_l]) > good_attr_l:
                test_element[0] = label_2
            else:
                test_element[0] = label_1

    return(list_test)

def distance(element1,element2):
    distance = 0.0
    temp = 0.0
    if len(element1[1]) != 0:
        for attr in range(len(element1[1])):
            temp = temp + (float(element1[1][attr])-float(element2[1][attr]))*(float(element1[1][attr])-float(element2[1][attr]))
        distance = math.sqrt(temp)
    return(distance)

def getmax(list):
    max_distance = 0.0
    max_label = 0
    indicator = 0
    for item in list:
        if indicator == 0:
            max_label = item[0]
            max_distance = item[1]
            indicator = indicator + 1
        else:
            if max_distance < item[1]:
                max_label = item[0]
                max_distance = item[1]
            elif max_distance == item[1]:
                if max_label < item[0]:
                    max_label = item[0]
                else:
                    continue
            else:
                continue
    return([max_label,max_distance])

'''
def getminlabel(list):
    min_distance = 0.0
    min_label = 0
    indicator = 0
    for item in list:
        if indicator == 0:
            min_label = item[0]
            min_distance = item[1]
            indicator = indicator + 1
        else:
            if min_distance > item[1]:
                min_label = item[0]
                min_distance = item[1]
            elif min_distance == item[1]:
                if min_label > item[0]:
                    min_label = item[0]
                else:
                    continue
            else:
                continue
    return(min_label)
'''

def k_neighbour(list_train,element):

    label_li = []
    indicator = 0
    for neighbour in list_train:
        if indicator != 3:
            temp = distance(neighbour,element)
            label_li.append([neighbour[0],temp])
            indicator = indicator + 1
        else:
            temp = distance(neighbour,element)
            max_li = getmax(label_li)
            if temp < max_li[1]:
                label_li.remove(max_li)
                label_li.append([neighbour[0],temp])
            elif temp == max_li[1]:
                if neighbour[0] < max_li[0]:
                    label_li.remove(max_li)
                    label_li.append([neighbour[0],temp])
                else:
                    continue
            else:
                continue
    return(label_li)

def main():
    (list_train,list_test) = readfile()
    print(list_train)
    print(list_test)
    prediction_tree = predict_label_tree(list_train,list_test)
    for test in prediction_tree:
        print(test[0])

    print('')

    for test in list_test:
        label_li = k_neighbour(list_train,test)
        label_only = []
        label_only.append(label_li[0][0])
        label_only.append(label_li[1][0])
        label_only.append(label_li[2][0])

        print_indicator = 0
        if label_only[0] == label_only[1]:
            print(label_only[0])
            print_indicator = 1
        if label_only[0] == label_only[2]:
            print(label_only[0])
            print_indicator = 1
        if label_only[1] == label_only[2]:
            print(label_only[1])
            print_indicator = 1


        if print_indicator == 0:
            if label_only[0] < label_only[1] and label_only[0] < label_only[2]:
                print(label_only[0])
            if label_only[1] < label_only[0] and label_only[1] < label_only[2]:
                print(label_only[1])
            if label_only[2] < label_only[0] and label_only[2] < label_only[1]:
                print(label_only[2])

if __name__== "__main__":
  main()
