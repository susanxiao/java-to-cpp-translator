#include <iostream>
#include <sstream>

#include "java_lang.h"
#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test023;
int main (int argc, char ** argv) {
	__rt::Array<String>* args = new __rt::Array<String>(argc-1);
	for (int a = 1; a < argc; a++) {
		String argument = new __String(argv[a]);
		args->__data[a-1] = argument;
	}

	__rt::Array<Object>* as = (__rt::Array<Object>*) args;

	for (int32_t i = 0; i < as->length; i++) {
		if (i < 0 || as->length <= i) throw java::lang::ArrayIndexOutOfBoundsException();
		cout << (String) as->__data[i] << endl;
	}
	return 0;
}