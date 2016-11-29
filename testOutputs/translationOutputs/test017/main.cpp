
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test017;

int main (int argc, char ** args) 
{

int main(void)
{

	A a = new __A(5);
	cout << a->__vptr->methodSelf(a) << endl;

	return 0;
}

//------------------

