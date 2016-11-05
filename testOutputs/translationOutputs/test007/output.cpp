
//------------------

#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs{
	namespace test007{

	__A::__A() : __vptr(&__vtable)  {

		a = new __String("A");
		
	}

	__A_VT __A::__vtable;

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


	__B::__B() : __vptr(&__vtable)  {

		b = new __String("B");
		
	}

	__B_VT __B::__vtable;

	String __B::toString(B __this) {
		std::ostringstream sout;
		sout << __this;
		return new __String(sout.str());
	}

	Class __B::__class() {
		static Class k =
			new __Class(__rt::literal("class inputs.javalang.B"), (Class) __rt::null());
 		return k;
	}


	}
}

//------------------

