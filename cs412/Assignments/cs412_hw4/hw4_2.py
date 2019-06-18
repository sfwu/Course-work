import sys
import math

N = 0

def readfile():
    global N
    firstline = 1
    round = 0
    point = []
    centroid = []

    for line in sys.stdin:
        line_list = line.split()
        if firstline == 1:
            firstline = 0
            NK_pair = line_list
        else:
            if round < int(NK_pair[0]):
                round = round + 1
                line_list[:] = [float(x) for x in line_list]
                point.append(line_list)
    N = int(NK_pair[0])
    return(point,int(NK_pair[1]))

def dist(point1,point2):
    distance = 0.0
    for coordinate in range(len(point1)):
        distance = distance + (float(point1[coordinate]) - float(point2[coordinate]))*(float(point1[coordinate]) - float(point2[coordinate]))
    d = math.sqrt(distance)
    return(d)

def dist_matrix(point):
    disM = []
    for p in range(len(point)):
        temp = []
        for q in range(len(point)):
            temp.append(dist(point[p],point[q]))
        disM.append(temp)
    return(disM)

def give_label(disM,k):
    global N

    label_list = []
    for i in range(N):
        label_list.append([i,[i]])
    while True:
        min_dist = 1000000000000.0
        Need_cluster = []
        for p in range(len(disM)):
            for q in range(len(disM)):
                #dont let distance 0 influce the result
                if p > q:
                    if disM[p][q] < min_dist:
                        min_dist = disM[p][q]
                        Need_cluster = [p,q]
                    elif disM[p][q] > min_dist:
                        continue
                    else:
                        if label_list[p][0] < label_list[Need_cluster[0]][0]:
                            Need_cluster = [p,q]
                        elif label_list[p][0] > label_list[Need_cluster[0]][0]:
                            continue
                        else:
                            if label_list[q][0] < label_list[Need_cluster[1]][0]:
                                Need_cluster = [p,q]
                            else:
                                continue
        #replace each distance by the smaller one
        if Need_cluster:
            if disM:
                for r in range(len(disM)):
                    if r != Need_cluster[0] and r != Need_cluster[1]:
                        if disM[Need_cluster[1]][r] < disM[Need_cluster[0]][r]:
                            disM[Need_cluster[0]][r] = disM[Need_cluster[1]][r]
                        if disM[r][Need_cluster[1]] < disM[r][Need_cluster[0]]:
                            disM[r][Need_cluster[0]] = disM[r][Need_cluster[1]]
        #delete the q row and the q column
        if Need_cluster:
            if disM:
                disM.pop(Need_cluster[1])
                for row in disM:
                    row.pop(Need_cluster[1])
        #combine two cluster
        if Need_cluster:
            if label_list[Need_cluster[0]][0] < label_list[Need_cluster[1]][0]:
                label_list[Need_cluster[0]][1] = label_list[Need_cluster[0]][1] + label_list[Need_cluster[1]][1]
            if label_list[Need_cluster[0]][0] > label_list[Need_cluster[1]][0]:
                label_list[Need_cluster[0]][0] = label_list[Need_cluster[1]][0]
                label_list[Need_cluster[0]][1] = label_list[Need_cluster[0]][1] + label_list[Need_cluster[1]][1]

        label_list.pop(Need_cluster[1])

        if len(label_list) == k:
            return(label_list)

def main():
    global N
    (point,k) = readfile()
    disM = dist_matrix(point)
    label_list = give_label(disM,k)
    result = []
    for i in range(N):
        result.append(i)
    for i in label_list:
        for j in i[1]:
            result[j] = i[0]
    for i in result:
        print(i)

if __name__ == '__main__':
    main()
