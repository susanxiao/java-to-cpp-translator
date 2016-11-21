
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test003;

int main(void)
{

	A a = new __A(new __String("A"));
	cout << a->__vptr->methodGetFld(a)->data << endl;

	return 0;
}

//------------------

