import sys
#we make the sequence_list as a global bariable since it remains the same all the time.
sequence_list = []
#the minsup is set to 2.
minsup = 2
#we want to create the first level appearence list_this_step
first_appearence_dic = {}

def readfile():
    list = []
    for line in sys.stdin:
        line_list = line.split()
        for item in line_list:
                list.append(item)
        list.append('\n')
    list.pop(-1)
    return(list)

#we want to make a list of sequences with each sequence being a list of item
def make_trans_set():
    global sequence_list
    item_list = []
    list = readfile()

    for item in list:
        if item != '\n':
            item_list.append(item)
        else:
            sequence_list.append(item_list)
            item_list = []
    #when we finish looping through the last sequence, we have to
    #put it into our list manually since there is no \n at the end
    sequence_list.append(item_list)

def make_appearence_dic():
    global first_appearence_dic
    global sequence_list
    global minsup
    sequence_index = 0
    for sequence in sequence_list:
        word_index = 0
        for word in sequence:
            if word in first_appearence_dic:
                first_appearence_dic[word].append((sequence_index,word_index))
            else:
                first_appearence_dic[word] = []
                first_appearence_dic[word].append((sequence_index,word_index))
            word_index = word_index + 1
        sequence_index = sequence_index + 1
    for single_item in first_appearence_dic.copy():
        if len(first_appearence_dic[single_item]) < minsup:
            del first_appearence_dic[single_item]

def old_spade():
    global first_appearence_dic
    global minsup
    result_dic = {}
    #we wwant to loop at most 4 times
    count = 4
    #transfer the first dic to result dic

    dic_this_step = first_appearence_dic
    while(count != 0):
        dic_next_step = {}
        for i in dic_this_step:
            for j in first_appearence_dic:
                for pos_i in dic_this_step[i]:
                    for pos_j in first_appearence_dic[j]:
                        string = ''
                        if pos_i[0] == pos_j[0]:
                            if pos_i[1] == pos_j[1] - 1:
                                string = i + ' ' + j
                                #print(string)
                                if string in dic_next_step:
                                    if (pos_j[0],pos_j[1]) in dic_next_step[string]:
                                        continue
                                    else:
                                        dic_next_step[string].append((pos_j[0],pos_j[1]))
                                else:
                                    dic_next_step[string] = []
                                    dic_next_step[string].append((pos_j[0],pos_j[1]))
            #print(dic_next_step)
        for item in dic_next_step.copy():
            if len(dic_next_step[item]) < minsup:
                del dic_next_step[item]
        result_dic = {**result_dic, **dic_next_step}
        dic_this_step = dic_next_step
        count = count - 1
    return(result_dic)

def spade():

    global first_appearence_dic
    global minsup
    result_dic = {}
    #we wwant to loop at most 4 times
    count = 4
    #transfer the first dic to result dic

    dic_this_step = first_appearence_dic
    while(count != 0):
        dic_next_step = {}
        for i in dic_this_step:
            for pos_i in dic_this_step[i]:
                if pos_i[1]+1 != len(sequence_list[pos_i[0]]):
                    if sequence_list[pos_i[0]][pos_i[1]+1] in first_appearence_dic:
                        string = ''
                        string = i + ' ' + sequence_list[pos_i[0]][pos_i[1]+1]
                        if string in dic_next_step:
                            if (pos_i[0],pos_i[1]+1) in dic_next_step[string]:
                                continue
                            else:
                                dic_next_step[string].append((pos_i[0],pos_i[1]+1))
                        else:
                            dic_next_step[string] = []
                            dic_next_step[string].append((pos_i[0],pos_i[1]+1))
        for item in dic_next_step.copy():
            if len(dic_next_step[item]) < minsup:
                del dic_next_step[item]
        result_dic = {**result_dic, **dic_next_step}
        dic_this_step = dic_next_step
        count = count - 1
    return(result_dic)


def sort_lili_sup(list_list):
    list_list = sorted(list_list, key=lambda x: x[0], reverse = True)
    return(list_list)

def sort_lili_key(list_list):
    list_list = sorted(list_list, key=lambda x: x[1])
    return(list_list)

def lets_print(result_dic):
    global minsup
    result_list = []
    temp_list = []

    for key in result_dic:
        temp_list.append([len(result_dic[key]),key])
    temp_list = sort_lili_sup(temp_list)
    indicator = 1
    list = []
    for item in temp_list:
        if list == []:
            list.append(item)
        else:
            if item[0] == list[0][0]:
                list.append(item)
            else:
                indicator = 0
        if indicator == 0:
            list = sort_lili_key(list)
            result_list = result_list + list
            list = [item]
            indicator = 1
    list = sort_lili_key(list)
    result_list = result_list + list

    print_count = 0
    for item in result_list:
        if print_count != 20:
            print(item)
            print_count = print_count + 1
        else:
            break

def main():
    global minsup
    global sequence_list
    make_trans_set()
    make_appearence_dic()
    #print(first_appearence_dic)
    #print(spade())
    lets_print(spade())

if __name__== "__main__":
  main()
