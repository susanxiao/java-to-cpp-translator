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
	java::lang::Class Array<inputs::test031::int>::__class() {
		static java::lang::Class k =
			new java::lang::__Class(literal("[java.lang.int;"),
					java::lang::__Object::__class(),
					inputs::test031::__int::__class());
		return k;
	}
}