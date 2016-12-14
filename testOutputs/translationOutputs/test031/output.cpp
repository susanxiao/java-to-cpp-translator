#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test031 {
	}
}

namespace __rt {
	template<>
	java::lang::Class Array< __rt::Array<int>* >::__class() {
		static java::lang::Class k =
			new java::lang::__Class(literal("[[Linputs.test031.int;"),
					java::lang::__Object::__class(),
					__rt::Array<int>::__class());
		return k;
	}
}