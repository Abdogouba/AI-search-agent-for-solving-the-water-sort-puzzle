This is a project I implemented in the AI course in the German University in Cairo CS department.
In this project, I implemented a search agent that solves
the water-sort puzzle. The puzzle consists of several bottles each consisting of several
layers of liquids of different colours. The objective is to make it so that in every bottle
all layers are of the same colour. The agent can achieve that by pouring from one bottle
to another.
Each bottle has a given capacity (maximum number of layers it can hold) and is assigned
an identifier number (0 to n) according to its order in the given initial state. The only
action the agent can do is pour from one bottle to another. In a given instance of the
game, all bottles have the same capacity but this capacity and the number of bottles
vary across instances of the problem. There are only 5 possible colors: red, green, blue,
yellow and orange.
Given an initial description of the state, the agent should be able to search for a plan
(if such a plan exists) to achieve its objective.
