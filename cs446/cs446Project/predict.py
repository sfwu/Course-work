import numpy as np
import pandas as pd

my_data = pd.read_csv('train.csv', index_col = 0)


my_data = my_data.drop(columns = 'batch_size_test')
my_data = my_data.drop(columns = 'batch_size_val')
my_data = my_data.drop(columns = 'criterion')
my_data = my_data.drop(columns = 'optimizer')
my_data = my_data.drop(columns = 'batch_size_train')

my_data = my_data.to_numpy()

print(my_data)