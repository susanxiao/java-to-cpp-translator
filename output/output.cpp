
//------------------

#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs{
	namespace test001{

	__A::__A()  :  __vptr(&__vtable) {}

	__A_VT __A::__vtable;

	String __A::toString(A __this) {
		std::ostringstream sout;
		sout << "A";
		return new __String(sout.str());
	}


	int32_t __A::hashCode(A __this){
		return 5;
	}

	Class __A::__class() {
		static Class k =
			new __Class(__rt::literal("class inputs.javalang.A"), (Class) __rt::null());
 		return k;
	}


	}
}

//------------------


