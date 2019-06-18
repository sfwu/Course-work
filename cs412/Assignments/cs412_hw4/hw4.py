import sys
import math

def readfile():
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
            else:
                line_list[:] = [float(x) for x in line_list]
                centroid.append(line_list)
    return(point,centroid)

def dist(point1,point2):
    distance = 0.0
    for coordinate in range(len(point1)):
        distance = distance + (float(point1[coordinate]) - float(point2[coordinate]))*(float(point1[coordinate]) - float(point2[coordinate]))
    d = math.sqrt(distance)
    return(d)

def give_label(point,centroid):
    item_label = {}
    round = 0
    indicator = 0
    while True:
        for v_index in range(len(point)):
            min_dist = 1000000000000
            for cent in range(len(centroid)):
                di = dist(centroid[cent],point[v_index])

                if di < min_dist:
                    min_dist = di
                    item_label[v_index] = cent
                elif di > min_dist:
                    continue
                else:
                    if cent < item_label[v_index]:
                        item_label[v_index] = cent

        new_centroid = []
        for cent in range(len(centroid)):
            mean_point = []
            count = 0
            for k,v in item_label.items():
                if v == cent:
                    count = count + 1
                    if not mean_point:
                        mean_point = point[k].copy()
                    else:
                        for i in range(len(point[k])):
                            mean_point[i] = float(mean_point[i]) + float(point[k][i])
            if count != 0:
                mean_point[:] = [x/count for x in mean_point]
                new_centroid.append(mean_point)
            if count == 0:
                new_centroid.append(centroid[cent])

        if round == 0:
            last_item_label = item_label.copy()
            centroid = new_centroid
        else:

            for key in item_label:
                if last_item_label[key] != item_label[key]:
                    indicator = 1
            if indicator == 0:
                return(item_label)
            else:
                last_item_label = item_label.copy()
                centroid = new_centroid

        round = round + 1
        indicator = 0
def main():
    (point,centroid) = readfile()
    item_label = give_label(point,centroid)
    for item in item_label:
        print(item_label[item])

if __name__ == '__main__':
    main()
