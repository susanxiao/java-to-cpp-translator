
//------------------

#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs{
	namespace test017{

	__A::__A(int x) : __vptr(&__vtable)  {

		self = this;

		
	}

	__A_VT __A::__vtable;

	A __A::self(A __this) {
		return  __this->self;
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


	}
}

//------------------

