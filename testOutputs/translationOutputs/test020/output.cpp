
//------------------

#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs{
	namespace test020{

	__A::__A()  :  __vptr(&__vtable) {}

	__A_VT __A::__vtable;

	int __A::x(A __this) {
	String __A::toString(A __this) {
		std::ostringstream sout;
		sout << __this;
		return new __String(sout.str());
	}

	Class __A::__class() {
		static Class k =
			new __Class(__rt::literal("class inputs.javalang.A"), (Class) __rt::null());
 		return k;
	}


	}
}

//------------------

