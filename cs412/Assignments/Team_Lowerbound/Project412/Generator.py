import numpy as np
import os
import random

def num2dna(num_list):
    DNA=['A','C','G','T']
    dna_list=[]
    for item in num_list:
        dna_list.append(DNA[item])
    return dna_list

def info_con(prob):
    return prob*np.log2(prob/0.25)

def solver_two_vari(sum_vari,sum_info):
    find_sym=0
    for i in range(int(sum_vari*1000)+1):
        j=int(sum_vari*1000)+1-i
        i=i/1000
        j=j/1000
        #print(i*np.log(i/0.25)+j*np.log(j/0.25))
        if abs(info_con(i)+info_con(j)-sum_info)<0.001:
            find_sym=1
            return (i,j)
    if find_sym==0:
        return 0
    
def gene_col(icpc):
    if icpc==np.log2(4):
        blank=[0,0,0,0]
        blank[random.randrange(0,4)]=1
        return blank
    elif icpc>np.log2(4):
        return 0
    else:
        while 1:
            num_1=random.randrange(0,1000)/1000
            num_2=random.randrange(0,int(1000*(1-num_1)))/1000
            if solver_two_vari(1-num_1-num_2,icpc-info_con(num_1)-info_con(num_2))==0:
                continue
            else:
                num_3,num_4=solver_two_vari(1-num_1-num_2,icpc-info_con(num_1)-info_con(num_2))
                return [num_1,num_2,num_3,1-(num_1+num_2+num_3)]   


def column_rera(arr):
    arr=arr.T
    for i in arr:
        random.shuffle(i)
    return arr.T

def ran_plant(site,sequence):
    planted_sequence=sequence[:]
    loc=random.randrange(0,len(planted_sequence)-len(site)+1)
    for i in range(len(site)):
        planted_sequence[i+loc]=site[i]
    return planted_sequence,loc

def generator (ICPC, ML, SL, SC, run):
    #DNA=['A','C','G','T']
    dic_sequences={}
    for seq in range(SC):
        dic_sequences.update({seq:num2dna([random.randrange(0,4) for i in range(SL)])})
        
    array_sequences=[]
    for key in dic_sequences.keys():
        array_sequences.append(dic_sequences[key])
        
    lists_pwm=[]
    for i in range(ML):
    #print('The no.',i,'is OK')
        lists_pwm.append(gene_col(ICPC))
    array_pwm=np.array(lists_pwm).T
    
    array_pwm=column_rera(array_pwm)
    
    dic_site={}
    for i in range(SC):
        temp_site=[]
        for j in range(ML):
            temp_site.append(np.random.choice(['A','C','G','T'], 1, p=array_pwm[:,j])[0])
        dic_site.update({i:(temp_site)})
        
    dic_sequences_planted={}
    list_location=[]
    for i in dic_sequences.keys():
        temp_planted,temp_loc=ran_plant(dic_site[i],dic_sequences[i])
        list_location.append(temp_loc)
        dic_sequences_planted.update({i:temp_planted})
 
    folder = "./"+'ICPC'+str(ICPC)+',ML'+str(ML)+',SC'+str(SC)+'.run'+str(run)   
    if not os.path.exists(folder):
        os.mkdir(folder)
        
    '''Write sequence'''
    
    path=''.join([folder,'/sequences.fa'])
    
    file = open(path, "w")
    for i in range(len(dic_sequences_planted.keys())) :
        file.write(">" + "sequence"+str(i+1) + "\n" +''.join(dic_sequences_planted[list(dic_sequences_planted.keys())[i]]) + "\n")
    file.close()
    
    
    '''Write site'''
    
    path=''.join([folder,'/sites.txt'])
    
    list_location=[str(i) for i in list_location]
    file = open(path, "w")
    file.write(','.join(list_location))
    file.close()
    
    '''Motif '''

    path=''.join([folder,'/motif.txt'])
    
    write_pwm=array_pwm.T
    file = open(path, "w")
    #file.write(','.join(list_location))
    #file.write(write_pwm)
    file.write('>MOTIF1  '+str(ML))
    for i in range(ML):
        file.write('\n')
        file.write('  '.join([str(i) for i in write_pwm[i]]))
    file.write('\n<')
    file.close()
    
    '''Motif '''    
    
    path=''.join([folder,'/motiflength.txt'])
    
    write_pwm=array_pwm.T
    file = open(path, "w")
    #file.write(','.join(list_location))
    #file.write(write_pwm)
    file.write(str(ML))
    file.close()


'''Main'''
SL=500
ICPC = 2 
ML = 8 
SL = 500 
SC = 10

for  ICPC in [ 1, 1.5, 2]:
    for run in range(1,11):
        generator(ICPC, ML, SL, SC, run)
        
SL=500
ICPC = 2 
ML = 8 
SL = 500 
SC = 10
for ML in [6,7,8]:
    for run in range(1,11):
        generator(ICPC, ML, SL, SC, run)    
        
SL=500
ICPC = 2 
ML = 8 
SL = 500 
SC = 10    
for SC in [ 5, 10, 20]:
    for run in range(1,11):
        generator(ICPC, ML, SL, SC, run)            
                
    
    
    
    
    