#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test008 {
		__A::__A() : __vptr(&__vtable),
			a((String)__rt::null()) {
			a = new __String("A");
			cout << a << endl;
		};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test008.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

		__B::__B() : __vptr(&__vtable),
			b((String)__rt::null()) {
			b = new __String("B");
			parent.a = new __String("B");
			cout << parent.a << endl;
		};

		Class __B::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test008.B"),__A::__class());
			return k;
		};

		__B_VT __B::__vtable;

	}
}

