import numpy as np


def evaluation(path):
    f_predmotif = path + "/predictedmotif.txt"
    f_predsites = path + "/predictedsites.txt"
    f_motif = path + "/motif.txt"
    f_sites = path + "/sites.txt"
    f_seq = path + "/sequences.fa"
    f_ml = path + "/motiflength.txt"
    f_time = path + "/timer.txt"

    f = open(f_seq, "r")
    seq_read = f.read()
    f.close()
    seq_read = seq_read.split()
    li_sequence = []
    help_list = ['A', 'C', 'G', 'T']
    for line in seq_read:
        line = list(line)
        num_line = []
        if not line[0] == '>':
            for letter in line:
                num_line.append(help_list.index(letter))
            li_sequence.append(num_line)

    f = open(f_ml, "r")
    ml_read = f.read()
    f.close()
    ml = int(ml_read)

    f = open(f_motif, "r")
    motif = []
    i = 0
    for line in f:
        prob = []
        if i != 0:
            for num in line.split():
                if num != '<':
                    prob.append(float(num))
            if prob != []:
                motif.append(prob)
        i += 1

    i = 0
    f = open(f_predmotif, 'r')
    pred_motif = []
    for line in f:
        prob = []
        if i != 0:
            for num in line.split():
                if num != '<':
                    prob.append(float(num))
            if prob != []:
                pred_motif.append(prob)
        i += 1

    with open(f_sites, "r") as f:
        num_site = f.readline().strip()
        num_site = num_site.split(",")

    sites = [int(index) for index in num_site]

    with open(f_predsites, "r") as f:
        num_site = f.readline().strip()
        num_site = num_site.split(",")
    pred_sites = [int(index) for index in num_site]

    overlap_pos = [max(ml - abs(pred_sites[i] - sites[i]), 0) for i in range(len(pred_sites))]
    overlapping_positions = sum(overlap_pos)

    overlap_sites = []
    for i in range(len(li_sequence)):
        pred_seq = li_sequence[i][sites[i]:(sites[i] + ml - 1)]
        orig_seq = li_sequence[i][pred_sites[i]:(pred_sites[i] + ml - 1)]
        overlap_site = sum(i == j for i, j in zip(pred_seq, orig_seq))
        overlap_sites.append(overlap_site)

    overlapping_sites = sum([1 if i >= ml/2 else 0 for i in overlap_sites])

    pred_motif = np.array(pred_motif)
    orig_motif = np.array(motif)
    orig_motif[orig_motif == 0.0] = 10**(-5)
    pred_motif[pred_motif == 0.0] = 10**(-5)

    kl = np.mean(np.sum(np.multiply(orig_motif, np.log2(np.divide(orig_motif, pred_motif))), axis=1), axis=0)

    f = open(f_time, "r")
    time_read = f.read()
    f.close()
    time = float(time_read)

    return kl, overlapping_positions, overlapping_sites, time

