#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test009 {
		__A::__A() : __vptr(&__vtable),
			self((A)__rt::null()) {
			self = this;
		};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test009.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}

