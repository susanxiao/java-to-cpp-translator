
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test007;

int main(void)
{

	B b = new __B();

	cout << b->parent.a->data << endl;

	cout << b->b->data << endl;

	return 0;
}

//------------------

