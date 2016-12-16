#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test041 {
		void __A::methodMObjectObject(A __this, Object o1, Object o2) {
			__rt::checkNotNull(o1);
			__rt::checkNotNull(__this);
			__rt::checkNotNull(o2);
			cout << "A.m(Object, Object)" << endl;
		};

		void __A::methodMAObject(A __this, A a1, Object o2) {
			__rt::checkNotNull(a1);
			__rt::checkNotNull(__this);
			__rt::checkNotNull(o2);
			cout << "A.m(A, Object)" << endl;
		};

		void __A::methodMObjectA(A __this, Object o1, A a2) {
			__rt::checkNotNull(a2);
			__rt::checkNotNull(o1);
			__rt::checkNotNull(__this);
			cout << "A.m(Object, A)" << endl;
		};

		__A::__A() : __vptr(&__vtable)
		{};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test041.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

		void __B::methodMObjectObject(B __this, Object o1, Object o2) {
			__rt::checkNotNull(o1);
			__rt::checkNotNull(__this);
			__rt::checkNotNull(o2);
			cout << "B.m(Object, Object)" << endl;
		};

		void __B::methodMBObject(B __this, B a1, Object o2) {
			__rt::checkNotNull(a1);
			__rt::checkNotNull(__this);
			__rt::checkNotNull(o2);
			cout << "B.m(B, Object)" << endl;
		};

		void __B::methodMObjectB(B __this, Object o1, B a2) {
			__rt::checkNotNull(a2);
			__rt::checkNotNull(o1);
			__rt::checkNotNull(__this);
			cout << "B.m(Object, B)" << endl;
		};

		__B::__B() : __vptr(&__vtable)
		{};

		Class __B::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test041.B"),__A::__class());
			return k;
		};

		__B_VT __B::__vtable;

		void __C::methodMCC(C __this, C c1, C c2) {
			__rt::checkNotNull(__this);
			__rt::checkNotNull(c1);
			__rt::checkNotNull(c2);
			cout << "C.m(C, C)" << endl;
		};

		__C::__C() : __vptr(&__vtable)
		{};

		Class __C::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test041.C"),__A::__class());
			return k;
		};

		__C_VT __C::__vtable;

	}
}

