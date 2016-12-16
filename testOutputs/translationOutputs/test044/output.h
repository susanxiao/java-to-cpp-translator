#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test044 {
		struct __A;
		struct __A_VT;

		struct __B;
		struct __B_VT;

		struct __C;
		struct __C_VT;


		typedef __A* A;
		typedef __B* B;
		typedef __C* C;

		struct __A {
			__A_VT* __vptr;

			__A();

			static Class __class();

			static __A_VT __vtable;

			static void methodM(A);
			static A methodMA(A, A);
		};

		struct __A_VT {
			Class __isa;

			int32_t (*hashCode)(A);
			bool (*equals)(A, Object);
			Class (*getClass)(A);
			String (*toString)(A);
			void (*methodM)(A);
			A (*methodMA)(A, A);

			__A_VT()
			: __isa(__A::__class()),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			toString((String(*)(A))&__Object::toString),
			methodM(&__A::methodM),
			methodMA(&__A::methodMA)
			{}
		};

		struct __B {
			__B_VT* __vptr;
			__A parent;

			__B();

			static Class __class();

			static __B_VT __vtable;

			static void methodM(B);
			static C methodMB(B, B);
			static A methodMA(B, A);
		};

		struct __B_VT {
			Class __isa;

			int32_t (*hashCode)(B);
			bool (*equals)(B, Object);
			Class (*getClass)(B);
			String (*toString)(B);
			void (*methodM)(B);
			A (*methodMA)(B, A);
			C (*methodMB)(B, B);

			__B_VT()
			: __isa(__B::__class()),
			hashCode((int32_t(*)(B))&__Object::hashCode),
			equals((bool(*)(B, Object))&__Object::equals),
			getClass((Class(*)(B))&__Object::getClass),
			toString((String(*)(B))&__Object::toString),
			methodM(&__B::methodM),
			methodMA(&__B::methodMA),
			methodMB(&__B::methodMB)
			{}
		};

		struct __C {
			__C_VT* __vptr;
			__A parent;

			__C();

			static Class __class();

			static __C_VT __vtable;

			static void methodM(C);
		};

		struct __C_VT {
			Class __isa;

			int32_t (*hashCode)(C);
			bool (*equals)(C, Object);
			Class (*getClass)(C);
			String (*toString)(C);
			void (*methodM)(C);
			A (*methodMA)(C, A);

			__C_VT()
			: __isa(__C::__class()),
			hashCode((int32_t(*)(C))&__Object::hashCode),
			equals((bool(*)(C, Object))&__Object::equals),
			getClass((Class(*)(C))&__Object::getClass),
			toString((String(*)(C))&__Object::toString),
			methodM(&__C::methodM),
			methodMA((A(*)(C, A))&__A::methodMA)
			{}
		};

	}
}
