import Expection
import os
import numpy as np
import matplotlib.pyplot as pl
cwd = os.getcwd()

default = [2, 8, 10]
icpc = [1, 1.5]
ml = [6, 7]
sc = [5, 20]
N = 11
Path_list = []
# get all the path of data sets in the order professor provided
for n in range(1, N):
    path = cwd + '/dataset/ICPC' + str(default[0]) + ',ML' + str(default[1]) +\
               ',SC' + str(default[2]) + '.run' + str(n)
    Path_list.append(path)

for ic in icpc:
    for n in range(1, N):
        path = cwd + '/dataset/ICPC' + str(ic) + ',ML' + str(default[1]) +\
               ',SC' + str(default[2]) + '.run' + str(n)
        Path_list.append(path)
for m in ml:
    for n in range(1, N):
        path = cwd + '/dataset/ICPC' + str(default[0]) + ',ML' + str(m) +\
               ',SC' + str(default[2]) + '.run' + str(n)
        Path_list.append(path)
for s in sc:
    for n in range(1, N):
        path = cwd + '/dataset/ICPC' + str(default[0]) + ',ML' + str(default[1]) + \
               ',SC' + str(s) + '.run' + str(n)
        Path_list.append(path)

n_para = 7
all_avg = []
all_std = []
for i in range(n_para):
    total = []
    total_std = []
    sub_dataset = Path_list[(i*10):(i*10+10)]
    for path in sub_dataset:
        total.append(list(Expection.evaluation(path)))
    total = np.array(total)
    avg = list(np.mean(total, axis=0))
    std = list(np.std(total, axis=0))
    all_avg.append(avg)
    all_std.append(std)
# all_avg is the average of every 10 data sets of each parameters and all_std is the std of every 10 data sets
# the return of evaluation is in the same order with the form professor provides
print(all_avg)
print(np.mean(all_avg, axis=0))
print(np.std(all_avg, axis=0))

# plot charts, prepare the data
# draw 3*4 plots
icpc = [1, 1.5, 2]
ml = [6, 7, 8]
sc = [5, 10, 20]

icpc_results = [all_avg[1], all_avg[2], all_avg[0]]
ml_results = [all_avg[3], all_avg[4], all_avg[0]]
sc_results = [all_avg[5], all_avg[0], all_avg[6]]

icpc_std = [all_std[1], all_std[2], all_std[0]]
ml_std = [all_std[3], all_std[4], all_std[0]]
sc_std = [all_std[5], all_std[0], all_std[6]]

Graph_x = [icpc, ml, sc]
Graph_y = [icpc_results, ml_results, sc_results]
Graph_std = [icpc_std, ml_std, sc_std]
x_label = ['ICPC', 'ML', 'SC']
y_label = ['KLD', 'Positions', 'Sites', 'Time']

fig, axes = pl.subplots(3, 4, figsize=(10, 6))

for i in range(3):
    for j in range(4):
        y = []
        std = []
        x = Graph_x[i]
        for m in range(3):
            y.append(Graph_y[i][m][j])
            std.append(Graph_std[i][m][j])
        axes[i, j].errorbar(x, y, std, marker='o', markersize=5)
        axes[i, j].set(xlabel=x_label[i], ylabel=y_label[j])

# show the plot on the screen
pl.show()

