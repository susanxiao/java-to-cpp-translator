
//------------------

#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs{
	namespace test012{

	__A::__A()  :  __vptr(&__vtable) {}

	__A_VT __A::__vtable;

	void __A::setA(A __this,String x) {
		__this->a = x;
	}


	__A::__A()  :  __vptr(&__vtable) {}

	__A_VT __A::__vtable;

	void __A::printOther(A other) {
		;
	}


	__A::__A()  :  __vptr(&__vtable) {}

	__A_VT __A::__vtable;

	String __A::myToString(A __this) {
		return  __this->a;
	}

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

	String __B1::toString(B1 __this) {
		std::ostringstream sout;
		sout << __this;
		return new __String(sout.str());
	}

	Class __B1::__class() {
		static Class k =
			new __Class(__rt::literal("class inputs.javalang.B1"), (Class) __rt::null());
 		return k;
	}

	String __B2::toString(B2 __this) {
		std::ostringstream sout;
		sout << __this;
		return new __String(sout.str());
	}

	Class __B2::__class() {
		static Class k =
			new __Class(__rt::literal("class inputs.javalang.B2"), (Class) __rt::null());
 		return k;
	}


	__C::__C()  :  __vptr(&__vtable) {}

	__C_VT __C::__vtable;

	String __C::myToString(C __this) {
		std::ostringstream sout;
		sout << "still C";
		return new __String(sout.str());
	}


	String __C::toString(C __this) {
		std::ostringstream sout;
		sout << __this;
		return new __String(sout.str());
	}

	Class __C::__class() {
		static Class k =
			new __Class(__rt::literal("class inputs.javalang.C"), (Class) __rt::null());
 		return k;
	}


	}
}

//------------------

