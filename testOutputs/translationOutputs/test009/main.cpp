
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test009;

int main (int argc, char ** args) 
{

	A a = new __A();

	cout << a->self << endl;

	return 0;
}

//------------------

