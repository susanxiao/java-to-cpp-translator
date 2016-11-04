#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test007 {
		struct __A;
		struct __A_VT;

		struct __B;
		struct __B_VT;


		typedef __A* A;
		typedef __B* B;

		struct __A {
			__A_VT* __vptr;
			String a;

			__A();

			static Class __class();

			static __A_VT __vtable;

		};

		struct __A_VT {
			Class __isa;

			bool (*equals)(A, Object);
			Class (*getClass)(A);
			int32_t (*hashCode)(A);
			String (*toString)(A);

			__A_VT()
			: __isa(__A::__class()),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			toString((String(*)(A))&__Object::toString)
			{}
		};

		struct __B {
			__B_VT* __vptr;
			String b;

			__B();

			static Class __class();

			static __B_VT __vtable;

		};

		struct __B_VT {
			Class __isa;

			bool (*equals)(B, Object);
			Class (*getClass)(B);
			int32_t (*hashCode)(B);
			String (*toString)(B);

			__B_VT()
			: __isa(__B::__class()),
			equals((bool(*)(B, Object))&__Object::equals),
			getClass((Class(*)(B))&__Object::getClass),
			hashCode((int32_t(*)(B))&__Object::hashCode),
			toString((String(*)(B))&__Object::toString)
			{}
		};

	}
}
