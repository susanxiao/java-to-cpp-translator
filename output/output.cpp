#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test020 {
		int32_t __A::x() {
			return 3;
		};

		__A::__A() : __vptr(&__vtable) {};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("class inputs.javalang.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}
