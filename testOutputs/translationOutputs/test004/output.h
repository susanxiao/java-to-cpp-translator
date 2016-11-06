#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test004 {
		struct __A;
		struct __A_VT;


		typedef __A* A;

		struct __A {
			__A_VT* __vptr;
			String fld;

			__A(String fld);

			static Class __class();

			static __A_VT __vtable;

			static String getFld(A);
		};

		struct __A_VT {
			Class __isa;

			bool (*equals)(A, Object);
			Class (*getClass)(A);
			String (*getFld)(A);
			int32_t (*hashCode)(A);
			String (*toString)(A);

			__A_VT()
			: __isa(__A::__class()),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			getFld(&__A::getFld),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			toString((String(*)(A))&__Object::toString)
			{}
		};

	}
}