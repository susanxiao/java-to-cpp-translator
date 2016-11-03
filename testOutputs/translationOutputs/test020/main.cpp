
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test020;

int main(void)
{

	int x;

	x = 3;

	cout << A->__vptr->x()->data << endl;

	return 0;
}

//------------------

