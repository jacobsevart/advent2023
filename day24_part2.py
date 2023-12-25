from sympy import *
from sympy.solvers import solvers

x, dx, y, dy, z, dz, t1, t2, t3 = symbols('x dx y dy z dz t1 t2 t3') # rock


system = [
    Eq(102352610405511 + 288*t1, x + dx*t1),
    Eq(225555584244738 + 97*t2, x + dx*t2),
    Eq(227688767181124 + 104*t3, x + dx*t3),

    Eq(202028623863107 + 172*t1, y + dy*t1),
    Eq(280452001678940 - 361*t2, y + dy*t2),
    Eq(208974144896814 - 84*t3, y + dy*t3),

    Eq(54441177479725 + 406*t1, z + dz*t1),
    Eq(158175058414862 + 177*t2, z + dz*t2),
    Eq(197748595030382 - 576*t3, z + dz*t3)
]

got = solve(system, (x, y, z, dx, dy, dz, t1, t2, t3))
print(got[0])
print(sum(got[0][0:3]))

