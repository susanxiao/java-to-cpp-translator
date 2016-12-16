#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test042 {
		void __A::methodM(A __this) {
			__rt::checkNotNull(__this);
			cout << "A.m()" << endl;
		};

		A __A::methodMA(A __this, A a) {
			__rt::checkNotNull(a);
			__rt::checkNotNull(__this);
			cout << "A.m(A)" << endl;
			return a;
		};

		__A::__A() : __vptr(&__vtable)
		{};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test042.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

		void __B::methodM(B __this) {
			__rt::checkNotNull(__this);
			cout << "B.m()" << endl;
		};

		B __B::methodMB(B __this, B b) {
			__rt::checkNotNull(b);
			__rt::checkNotNull(__this);
			cout << "B.m(B)" << endl;
			return b;
		};

		A __B::methodMA(B __this, A a) {
			__rt::checkNotNull(a);
			__rt::checkNotNull(__this);
			cout << "B.m(A)" << endl;
			return a;
		};

		__B::__B() : __vptr(&__vtable)
		{};

		Class __B::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test042.B"),__A::__class());
			return k;
		};

		__B_VT __B::__vtable;

	}
}

