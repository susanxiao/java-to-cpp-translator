#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test007 {
		__A::__A() : __vptr(&__vtable),
				a(new __String("A")) {
		};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("class inputs.javalang.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

		__B::__B() : __vptr(&__vtable),
				a(new __String("A")),
				b(new __String("B")) {
		};

		Class __B::__class() {
			static Class k =
			new __Class(__rt::literal("class inputs.javalang.B"), (Class) __rt::null());
			return k;
		};

		__B_VT __B::__vtable;

	}
}