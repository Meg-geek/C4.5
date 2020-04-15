import numpy
import pandas
import scipy.special
import math
import datetime

output_nodes_amount = 2


class neuralNetwork:
    def __init__(self, input_nodes_amount, first_hidden_nodes_amount, second_hidden_nodes_amount, learning_rate):
        # установили параметры нейросети
        self.input_nodes_amount = input_nodes_amount
        self.first_hidden_nodes_amount = first_hidden_nodes_amount
        self.second_hidden_nodes_amount = second_hidden_nodes_amount
        self.output_nodes_amount = output_nodes_amount
        self.learning_rate = learning_rate

        # задаем матрицы весов, заполянем случайными числами, положительными и отрицательными
        self.weight_input_first = numpy.random.normal(0.0,
                                                      pow(self.input_nodes_amount, -0.5),
                                                      (self.first_hidden_nodes_amount, self.input_nodes_amount))
        self.weight_first_second = numpy.random.normal(0.0,
                                                       pow(self.first_hidden_nodes_amount, -0.5),
                                                       (
                                                           self.second_hidden_nodes_amount,
                                                           self.first_hidden_nodes_amount))
        self.weight_second_output = numpy.random.normal(0.0,
                                                        pow(self.second_hidden_nodes_amount, -0.5),
                                                        (self.output_nodes_amount, self.second_hidden_nodes_amount))
        # функция активации - сигмоида
        self.activation_function = lambda x: scipy.special.expit(x)

        pass

    def query(self, inputs_list):
        inputs = numpy.array(inputs_list, ndmin=2).T

        # входные сигналы для 1 скрытого слоя
        hidden_first_inputs = numpy.dot(self.weight_input_first, inputs)
        # выходы на 1 слое
        hidden_first_outputs = self.activation_function(hidden_first_inputs)

        # второй слой
        hidden_second_inputs = numpy.dot(self.weight_first_second, hidden_first_outputs)
        hidden_second_outputs = self.activation_function(hidden_second_inputs)

        # выход
        final_inputs = numpy.dot(self.weight_second_output, hidden_second_outputs)
        final_outputs = self.activation_function(final_inputs)

        return final_outputs

    def train(self, inputs_list, targets_list, is_first_empty, is_second_empty):
        inputs = numpy.array(inputs_list, ndmin=2).T
        targets = numpy.array(targets_list, ndmin=2).T

        # входные сигналы для 1 скрытого слоя
        hidden_first_inputs = numpy.dot(self.weight_input_first, inputs)
        # выходы на 1 слое
        hidden_first_outputs = self.activation_function(hidden_first_inputs)

        # второй слой
        hidden_second_inputs = numpy.dot(self.weight_first_second, hidden_first_outputs)
        hidden_second_outputs = self.activation_function(hidden_second_inputs)

        # выход
        final_inputs = numpy.dot(self.weight_second_output, hidden_second_outputs)
        final_outputs = self.activation_function(final_inputs)
        output_errors = targets - final_outputs
        if is_first_empty:
            output_errors[0] = 0
        if is_second_empty:
            output_errors[1] = 0
        # ошибки второго скрытого слоя
        hidden_second_errors = numpy.dot(self.weight_second_output.T, output_errors)
        # первого скрытого слоя
        hidden_first_errors = numpy.dot(self.weight_first_second.T, hidden_second_errors)
        # обновляем веса
        self.weight_second_output += self.learning_rate * numpy.dot(
            (output_errors * final_outputs * (1.0 - final_outputs)), numpy.transpose(hidden_second_outputs))

        self.weight_first_second += self.learning_rate * numpy.dot(
            (hidden_second_errors * hidden_second_outputs * (1.0 - hidden_second_outputs)),
            numpy.transpose(hidden_first_outputs))

        self.weight_input_first += self.learning_rate * numpy.dot(
            (hidden_first_errors * hidden_first_outputs * (1.0 - hidden_first_outputs)), numpy.transpose(inputs))

        pass


training_data = pandas.read_csv(r"C:\Users\Маргарита\PycharmProjects\firstTry\editDataset.csv")
training_data = training_data[training_data['G_total'].notna() | training_data['КГФ'].notna()].reset_index(drop=True)
result_classes = training_data[['G_total', 'КГФ']].drop_duplicates().replace(numpy.nan, '', regex=True)
result_classes = [tuple(x) for x in result_classes.to_numpy()]
attributes_tuple = tuple(training_data.columns[:-2].to_numpy())
input_nodes_amount = 18
first_hidden_nodes_amount = 144
second_hidden_nodes_amount = 30
learning_rate = 0.01
network = neuralNetwork(input_nodes_amount, first_hidden_nodes_amount, second_hidden_nodes_amount, learning_rate)

attribute_values = {}
for attribute in attributes_tuple:
    attribute_values[attribute] = training_data[attribute].to_numpy()
attribute_values

max_attribute_values = {}
for attribute in attributes_tuple:
    max_attribute_values[attribute] = max(attribute_values[attribute])

max_attribute_values['Рпл. Тек (Карноухов)'] = 65.9


def get_input_data(example_index):
    input_data = []
    i = 0
    for attribute in attributes_tuple:
        value = attribute_values[attribute][example_index]
        if math.isnan(value):
            value = 0
        value = value / max_attribute_values[attribute]
        input_data.append(value)
        i += 1
    pass
    return input_data


max_g_total = max(training_data['G_total'])
max_kgf = max(training_data['КГФ'])
training_examples_amount = 92


def get_target_data(example_index):
    target_data = []
    if isinstance(training_data['G_total'][example_index], str):
        target_data.append(math.nan)
    else:
        target_data.append(training_data['G_total'][example_index] / max_g_total)
    if isinstance(training_data['КГФ'][example_index], str):
        target_data.append(math.nan)
    else:
        target_data.append(training_data['КГФ'][example_index] / max_kgf)
    return target_data


def calculate_learning_error(targets, final_outputs, is_first_empty, is_second_empty):
    targets = numpy.array(targets, ndmin=2).T
    output_errors = targets - final_outputs
    if is_first_empty:
        output_errors[0] = 0
    if is_second_empty:
        output_errors[1] = 0
    learning_error = 0
    for output_error in output_errors:
        learning_error += output_error * output_error
    learning_error = learning_error / 2
    return learning_error


def calculate_average_error():
    errors = []
    for k in range(training_examples_amount):
        input_data = get_input_data(k)
        target_data = get_target_data(k)
        final_outputs = network.query(input_data)
        errors.append(calculate_learning_error(target_data, final_outputs, math.isnan(target_data[0]),
                                               math.isnan(target_data[1])))
    average_error = sum(errors) / len(errors)
    return average_error


epoch = 1
training_examples_amount = 92
error = 1
time = datetime.datetime.now()

while error >= 0.0001 and epoch < 3000:
    for k in range(training_examples_amount):
        input_data = get_input_data(k)
        target_data = get_target_data(k)
        network.train(input_data, target_data, math.isnan(target_data[0]), math.isnan(target_data[1]))
    pass
    error = calculate_average_error()
    if epoch % 100 == 0:
        print(f'error is {error}, epoch {epoch}')
    epoch += 1
print(f'epochs : {epoch}')
print(f'final error is {error}')
print(f'time {datetime.datetime.now() - time}')

for k in range(training_examples_amount):
    input_data = get_input_data(k)
    print(f'result data in training set: {get_target_data(k)}')
    print(f'result data from network: {network.query(input_data)}')
pass
