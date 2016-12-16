#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test012 {
		void __A::methodSetA(A __this, String x) {
			__rt::checkNotNull(__this);
			__rt::checkNotNull(x);
			__this->a = x;
		};

		void __A::methodPrintOther(A __this, A other) {
			__rt::checkNotNull(other);
			__rt::checkNotNull(__this);
			cout << other->__vptr->methodMyToString(other) << endl;
		};

		String __A::methodMyToString(A __this) {
			__rt::checkNotNull(__this);
			return __this->a;
		};

		__A::__A() : __vptr(&__vtable),
			a((String)__rt::null())
		{};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test012.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

		__B1::__B1() : __vptr(&__vtable),
			b((String)__rt::null())
		{};

		Class __B1::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test012.B1"),__A::__class());
			return k;
		};

		__B1_VT __B1::__vtable;

		__B2::__B2() : __vptr(&__vtable),
			b((String)__rt::null())
		{};

		Class __B2::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test012.B2"),__A::__class());
			return k;
		};

		__B2_VT __B2::__vtable;

		String __C::methodMyToString(C __this) {
			__rt::checkNotNull(__this);
			return new __String("still C");
		};

		__C::__C() : __vptr(&__vtable),
			c((String)__rt::null())
		{};

		Class __C::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test012.C"),__B1::__class(),__A::__class());
			return k;
		};

		__C_VT __C::__vtable;

	}
}

