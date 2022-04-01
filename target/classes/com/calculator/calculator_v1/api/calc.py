from re import sub
import argparse


def parse_object():
    """ Return command line arguments object """
    
    argparser = argparse.ArgumentParser()
    argparser.add_argument('-e', '--expression', help="mathemical expression")

    return argparser.parse_args()


arg_datas = parse_object()

if not arg_datas.expression: 
	exit()

expression = arg_datas.expression

# expression is clean
# juste manage √ square symbol
# before evaluate

# replace '√' groups by ()**0.5
expression = sub(r"√([0-9]+)", r"(\1)**0.5", expression)
expression = sub(r"√\((.+)\)", r"(\1)**0.5", expression)

# space beetween groups of digits 4+5√5 -> 4 + 5 + 

# finaly print result

result = eval(expression)

if float(result).is_integer():
	result = int(result)

print(result)
