
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test004;

int main(void)
{

	A a = new __A(new __String("A"));
	cout << a->__vptr->getFld(a)->data << endl;

	return 0;
}

//------------------

