
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test022;

int main (int argc, char ** args) 
{

	//for statement
	for(int i=0; i<args.length ; i++)

	{
		cout << args[i] << endl;

	}

	return 0;
}

//------------------

